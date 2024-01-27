# language: ja
機能: ユーザのログイン
  ユーザはシステムを使用するためにログインする

シナリオ: ログインの成功
  前提 ユーザテーブルには次のデータが入っている:
    | USER_ID | USERNAME | LOGIN_ID | HASHED_PASSWORD | LOGIN_FLG |
    | 1000000001 | テスト太郎 | testId | password | 0 |
  もし ユーザが、ユーザID: "testId"、パスワード: "password" でログインした
  ならば APIキーが発行される

シナリオ: ログインの失敗
  前提 ユーザテーブルには次のデータが入っている:
    | USER_ID | USERNAME | LOGIN_ID | HASHED_PASSWORD | LOGIN_FLG |
    | 1000000002 | テスト太郎 | testId2 | password | 0 |
  もし ユーザが、ユーザID: "ngId"、パスワード: "password" でログインした
  ならば 例外になる
