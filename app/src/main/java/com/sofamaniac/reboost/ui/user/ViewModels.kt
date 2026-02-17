/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.user

import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.domain.repository.feed.CommentsRepository
import com.sofamaniac.reboost.domain.repository.feed.DownvotedRepository
import com.sofamaniac.reboost.domain.repository.feed.HiddenRepository
import com.sofamaniac.reboost.domain.repository.feed.SavedRepository
import com.sofamaniac.reboost.domain.repository.feed.UpvotedRepository
import com.sofamaniac.reboost.ui.subreddit.PostFeedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    repository: SavedRepository,
    visitedPostsDao: VisitedPostsDao
) : PostFeedViewModel(repository, visitedPostsDao)

@HiltViewModel
class CommentsViewModel @Inject constructor(
    repository: CommentsRepository,
    visitedPostsDao: VisitedPostsDao
) : PostFeedViewModel(repository, visitedPostsDao)

@HiltViewModel
class UpvotedViewModel @Inject constructor(
    repository: UpvotedRepository,
    visitedPostsDao: VisitedPostsDao
) : PostFeedViewModel(repository, visitedPostsDao)

@HiltViewModel
class DownvotedViewModel @Inject constructor(
    repository: DownvotedRepository,
    visitedPostsDao: VisitedPostsDao
) : PostFeedViewModel(repository, visitedPostsDao)

@HiltViewModel
class HiddenViewModel @Inject constructor(
    repository: HiddenRepository,
    visitedPostsDao: VisitedPostsDao
) : PostFeedViewModel(repository, visitedPostsDao)
