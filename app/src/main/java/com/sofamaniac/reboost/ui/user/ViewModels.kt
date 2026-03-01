/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.user

import com.sofamaniac.reboost.data.local.dao.VisitedCommunityDao
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
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao
) : PostFeedViewModel(id = null, repository, visitedPostsDao, visitedCommunityDao)

@HiltViewModel
class CommentsViewModel @Inject constructor(
    repository: CommentsRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao
) : PostFeedViewModel(id = null, repository, visitedPostsDao, visitedCommunityDao)

@HiltViewModel
class UpvotedViewModel @Inject constructor(
    repository: UpvotedRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao
) : PostFeedViewModel(id = null, repository, visitedPostsDao, visitedCommunityDao)

@HiltViewModel
class DownvotedViewModel @Inject constructor(
    repository: DownvotedRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao
) : PostFeedViewModel(id = null, repository, visitedPostsDao, visitedCommunityDao)

@HiltViewModel
class HiddenViewModel @Inject constructor(
    repository: HiddenRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao
) : PostFeedViewModel(id = null, repository, visitedPostsDao, visitedCommunityDao)
