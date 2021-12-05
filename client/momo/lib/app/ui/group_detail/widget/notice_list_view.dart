import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';
import 'package:momo/app/model/post/post.dart';
import 'package:momo/app/provider/post/post_paging_controller_provider.dart';
import 'package:momo/app/routes/routes.dart';
import 'package:momo/app/ui/components/status/loading_card.dart';
import 'package:momo/app/ui/components/status/no_item_card.dart';
import 'package:momo/app/util/navigation_service.dart';
import 'package:momo/app/util/theme.dart';

class NoticeListView extends ConsumerWidget {
  const NoticeListView({
    Key? key,
    required this.groupId,
  }) : super(key: key);

  final int groupId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final _paigingController =
        ref.watch(noticePaigingControllerProvider(groupId));

    return Container(
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
      height: 182,
      color: const Color(0xffffffff),
      child: Column(
        children: [
          _noticeTitle(),
          const SizedBox(height: 17),
          SizedBox(
            height: 86,
            child: PagedListView.separated(
              pagingController: _paigingController,
              scrollDirection: Axis.horizontal,
              builderDelegate: PagedChildBuilderDelegate<Post>(
                itemBuilder: (context, item, index) => _noticeCard(item),
                newPageProgressIndicatorBuilder: (context) => loadingCard(),
                firstPageProgressIndicatorBuilder: (context) => loadingCard(),
                noItemsFoundIndicatorBuilder: (context) => noItemCard(),
              ),
              separatorBuilder: (context, index) => const SizedBox(width: 14),
            ),
          ),
        ],
      ),
    );
  }

  Widget _noticeCard(Post post) {
    return InkWell(
      onTap: () {},
      child: Container(
        height: 86,
        width: 304,
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          color: MomoColor.main,
        ),
        child: Center(
          child: Text(
            post.contents,
            style: MomoTextStyle.defaultStyle.copyWith(
              color: MomoColor.white,
              height: 1.2,
            ),
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ),
    );
  }

  Widget _noticeTitle() {
    return Consumer(builder: (context, ref, _) {
      return Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            '공지사항',
            style: MomoTextStyle.subTitle,
          ),
          InkWell(
            onTap: () {
              ref
                  .read(navigatorProvider)
                  .navigateTo(routeName: AppRoutes.noticeList);
            },
            child: Transform.rotate(
              angle: pi,
              child: Icon(
                CupertinoIcons.back,
                color: MomoColor.black,
                size: 24.w,
              ),
            ),
          ),
        ],
      );
    });
  }
}
  // child: ListView.builder(
  //             scrollDirection: Axis.horizontal,
  //             itemBuilder: (context, index) {
  //               return InkWell(
  //                 onTap: () {
  //                   ref.read(navigatorProvider).navigateTo(
  //                         routeName: AppRoutes.postDetail,
  //                         arguments: index,
  //                       );
  //                 },
  //                 child: Container(
  //                   margin: const EdgeInsets.symmetric(horizontal: 4),
  //                   height: 86,
  //                   width: 304,
  //                   decoration: BoxDecoration(
  //                     borderRadius: BorderRadius.circular(16),
  //                     color: MomoColor.main,
  //                   ),
  //                   child: Center(
  //                     child: Text(
  //                       '공지사항',
  //                       style: MomoTextStyle.defaultStyle
  //                           .copyWith(color: MomoColor.white),
  //                     ),
  //                   ),
  //                 ),
  //               );
  //             },
  //             itemCount: 10,
  //           ),
