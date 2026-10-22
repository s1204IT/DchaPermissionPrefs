# Dcha 系アプリで独自権限持ってるやつ

リソースだけ依存関係の判別面倒くさかったので放置

+ permission:jp.co.benesse.touch.update.install.PERMISSION
  package:jp.co.benesse.touch.update.install
  protectionLevel:signature

+ permission:jp.co.benesse.touch.permission.ACCESS_SERVER_ASYNC
  package:jp.co.benesse.touch.asynccomm
  protectionLevel:signature

+ permission:jp.co.benesse.touch.permission.ACCESS_ACOUNT
  package:jp.co.benesse.touch.auth
  protectionLevel:signature

+ permission:jp.co.benesse.touch.common.anrsendservice.PERMISSION
  package:jp.co.benesse.touch.common.anrsendservice
  protectionLevel:signature

+ permission:jp.co.benesse.touch.permission.ACCESS_SERVER
  package:jp.co.benesse.touch.comm
  protectionLevel:signature
