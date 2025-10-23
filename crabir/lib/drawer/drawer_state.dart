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
    final account = context.watch<AccountsBloc>().state.account;
    if (account == null) {
      return Center(child: LoadingIndicator());
    }
    final icon =
        isSelectingAccount ? Icons.arrow_drop_up : Icons.arrow_drop_down;
    final settings = LateralMenuSettings.of(context);
    return Column(
      children: [
        ListTile(
          onTap: changeMode,
          leading: settings.showAccountAvatar
              ? CircleAvatar(
                  radius: 16,
                  backgroundColor: CrabirTheme.of(context).background,
                  foregroundImage: NetworkImage(account.profilePicture),
                )
              : null,
          title: settings.showAccountUsername ? Text(account.username) : null,
          trailing: Icon(icon),
        ),
        Divider(),
      ],
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
