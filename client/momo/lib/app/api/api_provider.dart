import 'dart:developer' as dp;

import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:momo/app/api/group_client/group_client.dart';
import 'package:momo/app/api/post_client/post_client.dart';
import 'package:momo/app/api/user_client/user_client.dart';
import 'package:momo/app/provider/auth/token_provider.dart';
import 'package:momo/main.dart';

final userClientProvider = Provider<UserClient>((ref) {
  final dio = ref.watch(dioProvider);
  return UserClient(dio, baseUrl: baseUrl!);
});

final groupClientProvider = Provider<GroupClient>((ref) {
  final dio = ref.watch(dioProvider);
  return GroupClient(dio, baseUrl: baseUrl!);
});

final postClientProvider = Provider<PostClient>((ref) {
  final dio = ref.watch(dioProvider);
  return PostClient(dio, baseUrl: baseUrl!);
});

final dioProvider = Provider<Dio>((ref) {
  final token = ref.watch(tokenProvider);
  final dio = Dio(BaseOptions(headers: {'Authorization': 'Bearer $token'}));
  dio.interceptors.add(CustomLogInterceptor());
  return dio;
});

class CustomLogInterceptor extends Interceptor {
  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    dp.log('REQUEST[${options.method}] => PATH: ${options.path}');
    dp.log('REQUEST DATA: ${options.data}');
    dp.log('REQUEST HEADER: ${options.headers}');
    dp.log('REQUEST PARAM: ${options.queryParameters}');
    dp.log('REQUEST URL: ${options.uri}');
    super.onRequest(options, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    dp.log(
        'RESPONSE[${response.statusCode}] => PATH: ${response.requestOptions.path}');
    dp.log('RESPONSE DATA => DATA: ${response.data}');
    super.onResponse(response, handler);
  }

  @override
  void onError(DioError err, ErrorInterceptorHandler handler) {
    dp.log(
        'ERROR[${err.response?.statusCode}] => PATH: ${err.requestOptions.path}');
    dp.log('ERROR headers: [${err.requestOptions.headers}]');
    dp.log('ERROR err: [${err.error}]');
    dp.log('ERROR msg: [${err.message}]');
    super.onError(err, handler);
  }
}
