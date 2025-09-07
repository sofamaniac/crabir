import 'dart:async';
import 'dart:convert';
import 'dart:math';

import 'package:crabir/accounts/user_account.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:flutter_web_auth_2/flutter_web_auth_2.dart';
import 'package:logging/logging.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:http/http.dart' as http;

const clientId = String.fromEnvironment("REDDIT_API_KEY");
const userAgent = String.fromEnvironment("USER_AGENT");
const redirectUri = "$userAgent://callback";
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
    return await db.insert("accounts", account.toJson(),
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  /// Removes an account from the database
  Future removeAccount(UserAccount account) async {
    final db = await database;
    return await db.delete(
      "accounts",
      where: "id = ?",
      whereArgs: [account.id],
    );
  }

  Future<List<UserAccount>> getAccounts() async {
    final db = await database;
    final maps = await db.query('accounts', orderBy: 'ord ASC');

    return maps.map((map) => UserAccount.fromJson(map)).toList();
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
    callbackUrlScheme: userAgent,
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

  final tokenData = json.decode(tokenResponse.body);
  final accessToken = tokenData["access_token"];
  final refreshToken = tokenData["refresh_token"];
  try {
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
  } catch (e) {
    Logger("Loggin to Reddit").severe("Failed to log in to reddit: $e");
    return false;
  }
}

String _generateRandomState([int length = 32]) {
  final rand = Random.secure();
  final values = List<int>.generate(length, (_) => rand.nextInt(256));
  return base64UrlEncode(values).replaceAll('=', '');
}
