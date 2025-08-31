import 'dart:async';
import 'dart:convert';
import 'dart:math';

import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:flutter_web_auth_2/flutter_web_auth_2.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:http/http.dart' as http;

const clientId = 'w0DROpe2H7uv2inVTvSfZw';
const redirectUri = 'com.sofamaniac.reboost://callback';
const baseUri = "www.reddit.com";
const authorizationEndpoint = '/api/v1/authorize';
const tokenEndpoint = '/api/v1/access_token';

const scopes = [
  "identity",
  "edit",
  "flair",
  "history",
  "modconfig",
  "modflair",
  "modlog",
  "modposts",
  "modwiki",
  "mysubreddits",
  "privatemessages",
  "read",
  "report",
  "save",
  "submit",
  "subscribe",
  "vote",
  "wikiedit",
  "wikiread",
];

class UserAccount {
  final String id;
  final String username;
  final String profilePicture;
  final String? accessToken;
  final String? refreshToken;

  bool get isAnonymous => id == "anonymous";

  const UserAccount(
    this.id,
    this.username,
    this.profilePicture,
    this.accessToken,
    this.refreshToken,
  );

  Map<String, dynamic> toMap() => {
        'id': id,
        'username': username,
        'profilePicture': profilePicture,
        'accessToken': accessToken,
        'refreshToken': refreshToken,
      };
  factory UserAccount.fromMap(Map<String, dynamic> json) => UserAccount(
        json['id'],
        json['username'],
        json['profilePicture'],
        json['accessToken'],
        json['refreshToken'],
      );

  factory UserAccount.fromUserInfo(
          UserInfo info, String accessToken, String refreshToken) =>
      UserAccount(info.id, info.name, info.iconImg, accessToken, refreshToken);

  factory UserAccount.anonymous() => UserAccount(
      "anonymous",
      "anonymous",
      "https://www.redditstatic.com/avatars/defaults/v2/avatar_default_1.png",
      null,
      null);
}

class AccountDatabase {
  Database? _db;

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB();
    return _db!;
  }

  Future<Database> _initDB() async {
    final dbPath = await getDatabasesPath();
    final tablePath = join(dbPath, "accounts.db");

    return await openDatabase(tablePath, version: 1,
        onCreate: (db, version) async {
      await db.execute('''
            CREATE TABLE IF NOT EXISTS accounts (
              ord INTEGER PRIMARY KEY ,
              id TEXT NOT NULL UNIQUE,
              username TEXT NOT NULL,
              profilePicture TEXT NOT NULL,
              accessToken TEXT,
              refreshToken TEXT
            )
        ''');
    });
  }

  Future<int> insertAccount(UserAccount account) async {
    final db = await database;
    return await db.insert("accounts", account.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<List<UserAccount>> getAccounts() async {
    final db = await database;
    final maps = await db.query('accounts', orderBy: 'ord ASC');

    return maps.map((map) => UserAccount.fromMap(map)).toList();
  }
}

/// Initiate log in to reddit. Returns `true` if process was successfull.
/// Throws an exception if the request might have been subject to a CSRF attack.
Future<bool> loginToReddit() async {
  final state = _generateRandomState();
  final authUrl = Uri.https(baseUri, authorizationEndpoint, {
    'client_id': clientId,
    'response_type': 'code',
    'state': state,
    'redirect_uri': redirectUri,
    'duration': 'permanent',
    'scope': scopes.join(" "),
  });

  // Open the browser for login
  final resultString = await FlutterWebAuth2.authenticate(
    url: authUrl.toString(),
    callbackUrlScheme: "com.sofamaniac.reboost",
  );

  final result = Uri.parse(resultString);

  if (result.queryParameters["state"] != state) {
    throw Exception(
      "The state we got back is different from what we expected. It might be a CSRF attack.",
    );
  }

  // Extract the code from the callback URL
  final code = result.queryParameters['code'];

  final header =
      'Basic ${base64Encode(utf8.encode('$clientId:'))}'; // No client secret for installed apps
  // Exchange the code for an access token
  final tokenResponse = await http.post(
    Uri.https(baseUri, tokenEndpoint),
    headers: {
      'Authorization': header, // No client secret for installed apps
    },
    body: {
      'grant_type': 'authorization_code',
      'code': code,
      'redirect_uri': redirectUri,
    },
  );

  // TODO return true if login was successfull and false otherwise

  final tokenData = json.decode(tokenResponse.body);
  final accessToken = tokenData["access_token"];
  final refreshToken = tokenData["refresh_token"];
  await RedditAPI.client().authenticate(refreshToken: refreshToken);
  final UserInfo userInfo = await RedditAPI.client().loggedUserInfo();
  await AccountDatabase().insertAccount(
    UserAccount.fromUserInfo(
      userInfo,
      accessToken,
      refreshToken,
    ),
  );
  return true;
}

String _generateRandomState([int length = 32]) {
  final rand = Random.secure();
  final values = List<int>.generate(length, (_) => rand.nextInt(256));
  return base64UrlEncode(values).replaceAll('=', '');
}
