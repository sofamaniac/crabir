part of 'drawer.dart';

class AccountView extends StatelessWidget {
  final UserAccount account;
  const AccountView({super.key, required this.account});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        CircleAvatar(child: Image.network(account.profilePicture)),
        Text(account.username)
      ],
    );
  }
}

class DrawerState extends State<AppDrawer> {
  bool _isSelectingAccount = false;

  bool get isSelectingAccount => _isSelectingAccount;

  Logger log = Logger("DrawerModel");

  DrawerState();

  void changeMode({bool? isSelectingAccount}) {
    setState(() {
      _isSelectingAccount = isSelectingAccount ?? !_isSelectingAccount;
    });
  }

  Widget currentAccountView(BuildContext context) {
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) {
        if (account.account != null) {
          final icon =
              isSelectingAccount ? Icons.arrow_drop_up : Icons.arrow_drop_down;
          return DrawerHeader(
            child: InkWell(
              onTap: changeMode,
              child: Row(
                spacing: 8,
                children: [
                  if (account.account != null) ...[
                    CircleAvatar(
                      radius: 32,
                      child: Image.network(account.account!.profilePicture),
                    ),
                    Text(account.account!.username)
                  ] else
                    const CircularProgressIndicator(),
                  Icon(icon),
                ],
              ),
            ),
          );
          // return UserAccountsDrawerHeader(
          //   decoration: BoxDecoration(color: Colors.black),
          //   currentAccountPicture: Image.network(account.account!.profilePicture),
          //   accountName: Text(account.account!.username),
          //   accountEmail: null,
          //   onDetailsPressed: changeMode,
          // );
        } else {
          return CircularProgressIndicator();
        }
      },
    );
  }

  Widget accountSelector(BuildContext context) {
    final state = context.watch<AccountsBloc>();
    if (state.selectedAccount == null) {
      return const CircularProgressIndicator();
    }
    return Expanded(
      child: ListView(
        children: [
          ...state.accounts.mapIndexed((index, account) => InkWell(
              onTap: () async {
                state.add(SelectAccount(index: index));
                // close menu after selection
                changeMode(isSelectingAccount: false);
              },
              child: account.build(context))),
          InkWell(
            onTap: () async {
              if (await loginToReddit()) {
                state.add(Initialize());
              }
            },
            child: Text("Add an account"),
          )
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          currentAccountView(context),
          isSelectingAccount ? accountSelector(context) : DrawerFeedSelection()
        ],
      ),
    );
  }
}
