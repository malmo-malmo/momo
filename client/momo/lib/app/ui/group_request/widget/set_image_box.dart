import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:momo/app/provider/group/group_request_provider.dart';
import 'package:momo/app/util/navigation_service.dart';
import 'package:momo/app/util/theme.dart';
import 'package:photo_manager/photo_manager.dart';

class SetImageBox extends ConsumerWidget {
  const SetImageBox({
    Key? key,
    required this.img,
  }) : super(key: key);

  Future<bool> photoManager() async {
    PermissionState result = await PhotoManager.requestPermissionExtend();
    if (result.isAuth) {
      return true;
    } else {
      return false;
    }
  }

  final String img;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Container(
      color: MomoColor.backgroundColor,
      width: double.infinity,
      height: 215.h,
      child: Stack(
        children: [
          img.isEmpty
              ? SizedBox(
                  height: 215.h,
                  width: double.infinity,
                  child: const Center(
                    child: Text('등록된 배경사진이 없습니다'),
                  ),
                )
              // : Image.file(
              //     File(img),
              //     height: 215.h,
              //     width: double.infinity,
              //     fit: BoxFit.none,
              //   ),
              : Image.network(
                  img,
                  height: double.infinity,
                  width: double.infinity,
                  fit: BoxFit.none,
                ),
          Align(
            alignment: Alignment.bottomRight,
            child: Padding(
              padding: const EdgeInsets.only(right: 24, bottom: 20),
              child: InkWell(
                onTap: () {
                  ref.read(navigatorProvider).pop();
                },
                child: InkWell(
                  onTap: () async {
                    final check = await photoManager();
                    if (check) {
                      // String? imagePath = await ref
                      //     .read(navigatorProvider)
                      //     .navigateTo(routeName: AppRoutes.gallery);
                      ref.read(groupRequestStateProvider.notifier).setImageUrl(
                          'http://ojsfile.ohmynews.com/CRI_T_IMG/2020/1211/A0002701462_T.jpg');
                    }
                  },
                  child: SvgPicture.asset(
                    'assets/icon/btn_camera_32.svg',
                    color: img.isEmpty ? null : const Color(0xffffffff),
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
