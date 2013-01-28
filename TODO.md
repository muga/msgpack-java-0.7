# MessagePack for Java 0.7

参考: [msgpack-java 改善案](http://togetter.com/li/445800)

## 1. Android 対応の強化
- アノテーションプロセッサ（apt）でTemplateをプリコンパイルする案
    * 開発環境がEclipse or mavenであることを仮定するとか、多少ドキュメントで対応するべき事案もあるかも
    * この辺りはJDOやJPAの実装が参考になるのかも

## 2. TemplateRegistry からの自動ルックアップの見直し
- TemplateRegistry#lookupXXX メソッドの見直し
    * Generics を含む Java の型システムに対応した低レイヤAPIを整理する必要がありそう
    * 実行時に取れる情報には限りがあるので、ユーザーに指定させるべき情報を洗い出す必要もあり
- 悪い例
    * List を lookup すると ListTemplate が見つかり、ArrayList でデシリアライズされる
    * LinkedList を lookup しても ArrayList でデシリアライズされる
    * Unpacker.read(Class) メソッドでも走り、unpacker.read(LinkedList.class) でも困る
- TemplateRegistry のキャッシュの実装がマズイ
- TemplateBuilder がシリアライズとデシリアライズの両方のメソッドを同時に生成する点
- Generics で <?> を使用すると pack できるけど、unpack が困難

## 3. msgpack-java のモジュール化
- TemplateRegistry や TemplateBuilder を別のパッケージとして切り出す
- msgpack-java のコアはシンプルにするとか
    * メンテナンス性やリリースサイクルの観点から好ましい
    * github 上で別リポジトリにしても良い
    * msgpack-java は Packer, Unpacker, Valueだけを含む
    * 低レイヤと便利機能群は分割しといたほうが良い

## 4. Packer/Unpacker の実装がかなり複雑でリファクタリングしたい
- msgpack-ruby (jruby) で採用した設計がシンプルに仕上がったので、そっちに寄せる案

## 5. Template の動的生成をシンプルに

## 6. アノテーションの整理
- 現状の実装はフィールド一覧を順番通りにシリアライズする「良きに計らう」実装でであるが、若干混乱を招いている印象
    * 何がシリアライズされるのか分かっていないと分からない
    * アノテーションで明示させた方が質問が減りそう
    * 言い換えれば、カスタムクラスのシリアライズするには基本的にアノテーションが必須など
