import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:momo/app/provider/bottom_index_provider.dart';
import 'package:momo/app/ui/calendar/calendar_page.dart';
import 'package:momo/app/ui/home/home_page.dart';
import 'package:momo/app/ui/my_meet/my_meet_page.dart';
import 'package:momo/app/ui/mypage/mypage.dart';
import 'package:momo/app/ui/search/search_page.dart';

class MainPage extends ConsumerWidget {
  const MainPage({Key? key}) : super(key: key);

  final _pages = const [
    HomePage(),
    SearchPage(),
    CalendarPage(),
    MyMeetPage(),
    Mypage(),
  ];

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final index = ref.watch(bottomIndexProvider);
    return SafeArea(
      child: Scaffold(
        body: _pages[index],
        bottomNavigationBar: Container(
          padding: const EdgeInsets.all(3),
          height: 56,
          decoration: const BoxDecoration(
            color: Color(0xffffffff),
            border: Border(
              top: BorderSide(
                color: Color(0xffe6e6e6),
                width: 1,
              ),
            ),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              bottomIcon(index: 0, icon: CupertinoIcons.home, title: '홈'),
              bottomIcon(index: 1, icon: CupertinoIcons.search, title: '검색'),
              bottomIcon(index: 2, icon: CupertinoIcons.calendar, title: '캘린더'),
              bottomIcon(
                  index: 3, icon: CupertinoIcons.person_2, title: '내 모임'),
              bottomIcon(index: 4, icon: CupertinoIcons.person, title: '마이페이지'),
            ],
          ),
        ),
      ),
    );
  }

  Widget bottomIcon({
    required int index,
    required IconData icon,
    required String title,
  }) {
    return Consumer(builder: (context, ref, _) {
      final bottomIndex = ref.watch(bottomIndexProvider);
      return Expanded(
        flex: 1,
        child: InkWell(
          onTap: () {
            ref.read(bottomIndexProvider.state).state = index;
          },
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                icon,
                color: bottomIndex == index
                    ? const Color(0xff846eaa)
                    : const Color(0xff000000),
              ),
              Text(
                title,
                style: TextStyle(
                  color: bottomIndex == index
                      ? const Color(0xff846eaa)
                      : const Color(0xff000000),
                  fontSize: 14,
                ),
              ),
            ],
          ),
        ),
      );
    });
  }
}
