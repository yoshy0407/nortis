# language: ja
機能: テナントの管理機能
  管理者はテナントの管理を行う

シナリオ: テナントの作成
  前提 テナントテーブルにはデータは存在しない
  もし 管理者が、テナント識別子: "testTenant"、テナント名: "テストテナント" でテナントの作成を依頼した
  ならば テナント識別子: "testTenant" でテナントが作成される