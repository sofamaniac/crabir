import 'package:crabir/loading_indicator.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/rule.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class ReportDialog extends StatefulWidget {
  final String subreddit;
  final RuleKind kind;
  final Fullname thing;

  const ReportDialog({
    super.key,
    required this.subreddit,
    this.kind = RuleKind.all,
    required this.thing,
  });

  @override
  State<ReportDialog> createState() => _ReportDialogState();
}

class _ReportDialogState extends State<ReportDialog> {
  late final Future<List<Rule>> _rules =
      RedditAPI.client().rules(name: widget.subreddit);
  Rule? _selected;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _rules,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return Center(child: LoadingIndicator());
        } else if (snapshot.hasData) {
          final rules = snapshot.data!
              .where(
                (rule) => rule.kind == RuleKind.all || rule.kind == widget.kind,
              )
              .toList();
          return AlertDialog.adaptive(
            title: Text("Report"),
            actions: [
              TextButton(onPressed: () => context.pop(), child: Text("Cancel")),
              TextButton(
                onPressed: _selected == null
                    ? null
                    : () {
                        RedditAPI.client().report(
                          name: widget.thing,
                          reason: _selected!.violationReason,
                        );
                        context.pop();
                      },
                child: Text("Submit"),
              )
            ],
            content: RadioGroup(
              groupValue: _selected,
              onChanged: (value) {
                setState(() {
                  _selected = value;
                });
              },
              child: ListView.builder(
                itemCount: rules.length,
                itemBuilder: (context, index) {
                  return RadioListTile(
                    value: rules[index],
                    title: Text(rules[index].violationReason),
                  );
                },
              ),
            ),
          );
        } else {
          return AlertDialog.adaptive(
            content: Text("Error while fetching rules: ${snapshot.error}"),
          );
        }
      },
    );
  }
}
