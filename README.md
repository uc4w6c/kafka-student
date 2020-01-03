kafka勉強用
参考URL
https://qiita.com/cohhei/items/fa254fa64cf88ab5f588
https://qiita.com/psyashes/items/e50bdfe35a2e7778986d

ローカル
$ ifconfig
確認したIPをdocker-compose.ymlに書き換え
例：192.168.11.5だったと仮定

$ docker-compose up -d
$ docker-compose ps
でzookeeperのIPを確認
kafka-student_zookeeper_1   /bin/sh -c /usr/sbin/sshd  ...   Up      0.0.0.0:32768->2181/tcp, 22/tcp, 2888/tcp, 3888/tcp
これだったら32768がIP

------------------------------------------------------------------------------------------------------------------
$ ./start-kafka-shell.sh 192.168.11.5 192.168.11.5:32768

kafka client
$ $KAFKA_HOME/bin/kafka-topics.sh --create --topic test --partitions 4 --zookeeper $ZK --replication-factor 1
この場合は testというトピック名でTopicを作成

$ $KAFKA_HOME/bin/kafka-topics.sh --describe --topic test --zookeeper $ZK
これでトピックが作成されたか確認

producer(kafka clientと同じホスト上)
$ $KAFKA_HOME/bin/kafka-console-producer.sh --topic=test --broker-list=`broker-list.sh`
producerとして接続した状態。コマンド受付状態になる


------------------------------------------------------------------------------------------------------------------
$ ./start-kafka-shell.sh 192.168.11.5 192.168.11.5:32768

consumer
$ $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server=`broker-list.sh` --topic=test --from-beginning
この状態でproducerでコマンド入力すると
consumer側に送信されてくる


------------------------------------------------------------------------------------------------------------------
java
送信する
$ java -cp build/libs/javaapi-1.0-SNAPSHOT-all.jar com.example.chapter4.FirstAppProducer

受信する
$ java -cp build/libs/javaapi-1.0-SNAPSHOT-all.jar com.example.chapter4.FirstAppConsumer

