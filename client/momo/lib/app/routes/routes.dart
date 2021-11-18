import 'package:flutter/material.dart';
import 'package:momo/app/ui/gallery/gallery_page.dart';
import 'package:momo/app/ui/login/category_page.dart';
import 'package:momo/app/ui/login/info_page.dart';
import 'package:momo/app/ui/login/login_page.dart';
import 'package:momo/app/ui/login/splash_screen.dart';
import 'package:momo/app/ui/login/terms_page.dart';
import 'package:momo/app/ui/main_page.dart';
import 'package:momo/app/ui/meeting_detail/meeting_detail_page.dart';
import 'package:momo/app/ui/meeting_list/meeting_list_page.dart';
import 'package:momo/app/ui/new_meet/new_meeting_page.dart';
import 'package:momo/app/ui/onboarding/onboarding_page.dart';
import 'package:momo/app/ui/request_meeting/request_meeting_page.dart';

class AppRoutes {
  static const main = '/main';
  static const splash = '/splash';
  static const login = '/login';
  static const trems = '/trems';
  static const category = '/category';
  static const info = '/info';
  static const onboarding = '/onboarding';
  static const meetingList = '/meetingList';
  static const meetingDetail = '/meetingDetail';
  static const newMeet = '/newMeet';
  static const gallery = '/gallery';
  static const requestMeeting = '/requestMeeting';
}

class AppRouter {
  static Route<dynamic>? onGenerateRoute(
    settings,
  ) {
    switch (settings.name) {
      case AppRoutes.main:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const MainPage(), settings: settings);
      case AppRoutes.splash:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const SplashPage(), settings: settings);
      case AppRoutes.login:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const LoginPage(), settings: settings);
      case AppRoutes.trems:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const TermsPage(), settings: settings);
      case AppRoutes.category:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const CategoryPage(), settings: settings);
      case AppRoutes.info:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const InfoPage(), settings: settings);
      case AppRoutes.onboarding:
        return MaterialPageRoute<dynamic>(
            builder: (_) => const OnboardingPage(), settings: settings);
      case AppRoutes.meetingList:
        final name = settings.arguments;
        return MaterialPageRoute<dynamic>(
          builder: (_) => MeetingListPage(name: name),
          settings: settings,
        );
      case AppRoutes.meetingDetail:
        return MaterialPageRoute<dynamic>(
          builder: (_) => const MeetingDetailPage(),
        );
      case AppRoutes.newMeet:
        return MaterialPageRoute<dynamic>(
          builder: (_) => const NewMeetingPage(),
        );
      case AppRoutes.gallery:
        return MaterialPageRoute<dynamic>(
          builder: (_) => const GalleryPage(),
        );
      case AppRoutes.requestMeeting:
        final arg = settings.arguments;
        return MaterialPageRoute<dynamic>(
          builder: (_) => RequestMeetingPage(
            img: arg,
          ),
        );
    }
  }
}
