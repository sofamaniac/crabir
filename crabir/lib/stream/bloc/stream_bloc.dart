import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:logging/logging.dart';

part 'stream_state.dart';
part 'stream_event.dart';
part 'stream_bloc.freezed.dart';

class StreamBloc extends Bloc<StreamEvent, StreamState> {
  StreamBloc({required this.streamable}) : super(const StreamState()) {
    on<Fetch>(_onFetched);
    on<Refresh>(_onRefresh);
    on<Vote>(_onVote);
    on<Save>(_onSave);
    on<SetStream>(_onSetStream);
  }

  Logger log = Logger("StreamBloc");

  reddit_stream.Streamable streamable;

  Future<void> _onFetched(Fetch event, Emitter<StreamState> emit) async {
    if (state.hasReachedMax) {
      return emit(state);
    }

    final hasReachedMax = !await streamable.next();
    final things = await streamable.getAll();
    emit(
      state.copyWith(
        status: StreamStatus.success,
        items: things,
        hasReachedMax: hasReachedMax,
      ),
    );
  }

  Future<void> _onRefresh(Refresh event, Emitter<StreamState> emit) async {
    await streamable.refresh();
    emit(StreamState());
  }

  Future<void> _onSave(Save event, Emitter<StreamState> emit) async {
    try {
      await streamable.save(
        name: event.name,
        save: event.saved,
        client: RedditAPI.client(),
      );
      final things = await streamable.getAll();
      emit(state.copyWith(items: things));
    } catch (_) {
      emit(state.copyWith(status: StreamStatus.failure));
    }
  }

  Future<void> _onVote(Vote event, Emitter<StreamState> emit) async {
    try {
      await streamable.vote(
        name: event.name,
        direction: event.direction,
        client: RedditAPI.client(),
      );
      final things = await streamable.getAll();
      emit(state.copyWith(items: things));
    } catch (_) {
      emit(state.copyWith(status: StreamStatus.failure));
    }
  }

  Future<void> _onSetStream(
    SetStream streamable,
    Emitter<StreamState> emit,
  ) async {
    this.streamable = streamable.streamable;
    emit(StreamState());
  }
}
