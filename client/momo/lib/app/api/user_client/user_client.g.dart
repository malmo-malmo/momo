// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_client.dart';

// **************************************************************************
// RetrofitGenerator
// **************************************************************************

class _UserClient implements UserClient {
  _UserClient(this._dio, {this.baseUrl}) {
    baseUrl ??= 'http://10.0.2.2:8080/api';
  }

  final Dio _dio;

  String? baseUrl;

  @override
  Future<dynamic> updateUserInfo(userInfoRequest) async {
    const _extra = <String, dynamic>{};
    final queryParameters = <String, dynamic>{};
    final _headers = <String, dynamic>{};
    final _data = <String, dynamic>{};
    _data.addAll(userInfoRequest.toJson());
    final _result = await _dio.fetch(_setStreamType<dynamic>(
        Options(method: 'PATCH', headers: _headers, extra: _extra)
            .compose(_dio.options, '/user',
                queryParameters: queryParameters, data: _data)
            .copyWith(baseUrl: baseUrl ?? _dio.options.baseUrl)));
    final value = _result.data;
    return value;
  }

  @override
  Future<dynamic> updateCategory(categoryRequest) async {
    const _extra = <String, dynamic>{};
    final queryParameters = <String, dynamic>{};
    final _headers = <String, dynamic>{};
    final _data = <String, dynamic>{};
    _data.addAll(categoryRequest.toJson());
    final _result = await _dio.fetch(_setStreamType<dynamic>(
        Options(method: 'PATCH', headers: _headers, extra: _extra)
            .compose(_dio.options, '/user/categories',
                queryParameters: queryParameters, data: _data)
            .copyWith(baseUrl: baseUrl ?? _dio.options.baseUrl)));
    final value = _result.data;
    return value;
  }

  @override
  Future<List<CodeNamePair>> getLocations() async {
    const _extra = <String, dynamic>{};
    final queryParameters = <String, dynamic>{};
    final _headers = <String, dynamic>{};
    final _data = <String, dynamic>{};
    final _result = await _dio.fetch<List<dynamic>>(
        _setStreamType<List<CodeNamePair>>(
            Options(method: 'GET', headers: _headers, extra: _extra)
                .compose(_dio.options, '/user/locations',
                    queryParameters: queryParameters, data: _data)
                .copyWith(baseUrl: baseUrl ?? _dio.options.baseUrl)));
    var value = _result.data!
        .map((dynamic i) => CodeNamePair.fromJson(i as Map<String, dynamic>))
        .toList();
    return value;
  }

  @override
  Future<bool> validateNickname(nickname) async {
    const _extra = <String, dynamic>{};
    final queryParameters = <String, dynamic>{};
    final _headers = <String, dynamic>{};
    final _data = <String, dynamic>{};
    final _result = await _dio.fetch<bool>(_setStreamType<bool>(
        Options(method: 'GET', headers: _headers, extra: _extra)
            .compose(_dio.options, '/user/validate/nickname/$nickname',
                queryParameters: queryParameters, data: _data)
            .copyWith(baseUrl: baseUrl ?? _dio.options.baseUrl)));
    final value = _result.data!;
    return value;
  }

  @override
  Future<List<University>> getUniversities(universityName) async {
    const _extra = <String, dynamic>{};
    final queryParameters = <String, dynamic>{
      r'universityName': universityName
    };
    final _headers = <String, dynamic>{};
    final _data = <String, dynamic>{};
    final _result = await _dio.fetch<List<dynamic>>(
        _setStreamType<List<University>>(
            Options(method: 'GET', headers: _headers, extra: _extra)
                .compose(_dio.options, '/universities',
                    queryParameters: queryParameters, data: _data)
                .copyWith(baseUrl: baseUrl ?? _dio.options.baseUrl)));
    var value = _result.data!
        .map((dynamic i) => University.fromJson(i as Map<String, dynamic>))
        .toList();
    return value;
  }

  RequestOptions _setStreamType<T>(RequestOptions requestOptions) {
    if (T != dynamic &&
        !(requestOptions.responseType == ResponseType.bytes ||
            requestOptions.responseType == ResponseType.stream)) {
      if (T == String) {
        requestOptions.responseType = ResponseType.plain;
      } else {
        requestOptions.responseType = ResponseType.json;
      }
    }
    return requestOptions;
  }
}
