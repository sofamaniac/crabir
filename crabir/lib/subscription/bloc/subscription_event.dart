part of 'subscription_bloc.dart';

@freezed
abstract class SubscriptionEvent with _$SubscriptionEvent {
  factory SubscriptionEvent.initialize() = Initialize;
  factory SubscriptionEvent.changed(bool isSubscribed) = Changed;
}
