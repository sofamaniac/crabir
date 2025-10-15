part of 'subscription_bloc.dart';

class SubscriptionState extends Equatable {
  final bool isLoading;
  final bool isSubscribed;

  const SubscriptionState._({
    required this.isLoading,
    required this.isSubscribed,
  });

  const SubscriptionState.loading()
      : this._(isLoading: true, isSubscribed: false);
  const SubscriptionState.subscribed()
      : this._(isLoading: false, isSubscribed: true);
  const SubscriptionState.unsubscribed()
      : this._(isLoading: false, isSubscribed: false);

  @override
  List<Object?> get props => [isLoading, isSubscribed];
}
