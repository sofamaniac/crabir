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
                      foregroundImage:
                          NetworkImage(account.account!.profilePicture),
                    ),
                    Text(account.account!.username)
                  ] else
                    const LoadingIndicator(),
                  Icon(icon),
                ],
              ),
            ),
          );
        } else {
          return LoadingIndicator();
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          currentAccountView(context),
          if (isSelectingAccount)
            Expanded(
              child: AccountSelector(
                showCurrentAccount: false,
                onTapCallback: () {
                  changeMode(isSelectingAccount: false);
                  widget.onAccountChanged?.call();
                },
              ),
            )
          else
            DrawerFeedSelection()
        ],
      ),
    );
  }
}
