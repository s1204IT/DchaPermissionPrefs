CREATE TABLE [T_SendReq] (
[Id] INTEGER PRIMARY KEY AUTOINCREMENT, -- ID
[Url] TEXT NOT NULL, -- APIのURL
[Api] TEXT, -- 呼び出すAPI
[Request] TEXT NOT NULL, -- APIに送るリクエスト
[Replace_flag] INTEGER NOT NULL DEFAULT '0' --置換フラグ
);