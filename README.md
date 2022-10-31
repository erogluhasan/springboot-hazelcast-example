# Spring Boot ile Hazelcast Distributed Cache Kullanımı

Hazelcast, popüler ikinci seviye önbellek mekanizmalarından biridir. Bu yazıda Hazelcast Cache Spring Boot örneğini göreceğiz. Ayrıca varlığı bir key-value çifti olarak depolamak için cacheleri yazıp okumanın nasıl kullanılacağını da göreceğiz.

## _Hazelcast Spring Boot dependencies_
Hazelcast kullanmanın 2 yöntemi vardır. Biri notasyonlar ile yaparak diğeri ise kendimizin yönettiği bir süreç. Notasyon kullanarak geliştirmek istiyorsak, `@EnableCaching` ekleyerek bu notasyonları spring boot uygulamasında kullanımını aktif etmek gerekmektedir. Hazelcasti uygulamamıza eklemek için aşağıdaki dependency’leri eklememiz gerekmektedir.

```xml
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>3.11.4</version>
</dependency>
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-spring</artifactId>
</dependency>
```
## Hazelcast Configuration

Hazelcast dağıtık cache kullanılması durumunda minumum 3 sunuculu kullanılmasını önermektedir ( https://hazelcast.com/blog/cluster-quorum/ ). Hazelcasti uygulamamız da kullanabilmek için bazı düzenlemeler yapmamız gerekmektedir. Hazelcast configurasyon dosyası;

```java
@Configuration
public class HazelcastConfig {
    @Bean
    public Config hazelCastConfig() {
        Config config = new Config();
        config.getManagementCenterConfig().setEnabled(true);
        config.getManagementCenterConfig().setUrl("http://localhost:8080/hazelcast-mancenter");
        config.setInstanceName("hazelcast-instance")
                .addMapConfig(
                        new MapConfig()
                                .setName("doctorCache")
                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setTimeToLiveSeconds(-1));
        NetworkConfig network = config.getNetworkConfig();
        network.setPortCount(3);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().addMember("127.0.0.1").addMember("127.0.0.1").addMember("127.0.0.1").setEnabled(true);
        network.getInterfaces().setEnabled(true).addInterface("127.0.0.1").addInterface("127.0.0.1").addInterface("127.0.0.1");
        return config;
    }
}
```
Aşağıda buradaki kullanılan her şeyin açıklamalarını yapacağız.
## Hazelcast Management Center Nedir ?
Hazelcast Management Center, Hazelcast çalıştıran küme üyelerinizi izlemenizi ve yönetmenizi sağlar. Kümelerinizin genel durumunu izlemenin yanı sıra, veri yapılarınızı ayrıntılı olarak analiz edebilir ve bunlara göz atabilir, harita yapılandırmalarını güncelleyebilir ve üyelerden iş parçacığı dökümleri alabilirsiniz.

Bunu kullanabilmek için önce uygulamayı Resmi web sitesinden indirmeniz gerekir (https://hazelcast.org/imdg/download/ ).
İndirildikten ve dosyayı çıkardıktan sonra, bin/start.sh dosyasını (veya Windows'ta bin/start.bat) çalıştırın.Varsayılan olarak, Uygulama 8080 portu ile ayağa kalkar bunu değiştirmek için JAVA_OPTS="-Dhazelcast.mc.http.port=8888"  şeklinde ayarlamak gerekir.

```sh
docker run --rm -m 512m -p 8080:8080 hazelcast/management-center
```
Management center başladığında tarayıcıya http://localhost:8080/hazelcast-mancenter yazarsanız aşağıdaki gibi ekran gelicektir bu ekranda kendi belirlediğiniz kullanıcı adı ve şifre ile devam edebilirsiniz.

## Hazelcast Management Center Config
Hazelcast konfigürasyon dosyamızdan management center ile bağlantıyı sağlaması için aşağıdakileri setlememiz gerekmektedir.

```java
config.getManagementCenterConfig().setEnabled(true);
config.getManagementCenterConfig().setUrl("http://localhost:8080/hazelcast-mancenter");
```

Burada dikkat edilmesi gereken birkaç nokta bulunmaktadır. Spring 1.5.6 versiyonunda 4.0.1 hazelcast versiyonu kullansanız bile management center için 3.7.8 versiyonu ile bağlantı kurmaktadır. Bu versiyonda url localhost:8080/hazelcast-mancenter şeklinde olmasına rağmen 4.0.1 versiyonun da localhost:8080 olarak bağlantı kurmaktadır.

## _Hazelcast Instance Adı Belirleme ve Map_

```java
config.setInstanceName("hazelcast-instance")
        .addMapConfig(
                new MapConfig()
                        .setName("doctorCache")
                        .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setTimeToLiveSeconds(-1));
```
`setInstanceName:` Oluşturalacak instance ye isim vermemizi sağlar. Bağlantı sağlanacak sunucuların isimleri verilebilir.
`addMapConfig:` Hazelcast verileri nasıl tutmak istediğinize göre çok fazla seçenek sunmaktadır. Biz map olarak tuttuğumuzdan bu şekilde kullandık. Diğer konfigürasyonları https://docs.hazelcast.org/docs/3.12.7/manual/html-single/index.html#distributed-data-structures bu linkten inceleyebilirsiniz.
`setName:` Tutulacak cacheyi hangi map isminde tutmak istediğimizi belirtiriz.
`setMaxSizeConfig:` Tutulmak istenen verinin giriş sayısını ve maxsize politikasına göre boyutunu belirtir.
`MaxSizePolicy:`
`setTimeToLiveSeconds:` Kullandığımız cachedeki verilerin ne kadar saniye hayatta kalmasını belirler ve o saniye dolduğunda o veri silinir hiç silinmesini istemiyorsak -1 vermemiz gerekmektedir.
`setEvictionPolicy:` Maxsize a göre cache belleği dolduğunda hangi sıralama ile silmek istediğimizi belirliyoruz.
`EvictionPolicy.LRU` kullandık biz bu eklenme sırasına göre bellek dolduğunda ilk ekleneni çıkarıcak şekilde ayarlanmıştır.
- `EvictionPolicy.LFU:` En az kullanılandan başlar silmeye.
- `EvictionPolicy.LRU:` İlk eklenileni siler.
- `EvictionPolicy.NONE:` Hafıza dolduğunda herhangi bir işlem yapmaz.
- `EvictionPolicy.RANDOM:` Rastgele olarak verileri siler.


## _Hazelcast Network Config_

```java
NetworkConfig network = config.getNetworkConfig();
network.setPortCount(3);
JoinConfig join = network.getJoin();
join.getMulticastConfig().setEnabled(false);
join.getTcpIpConfig().addMember("127.0.0.1").addMember("127.0.0.1").addMember("127.0.0.1").setEnabled(true);
network.getInterfaces().setEnabled(true).addInterface("127.0.0.1").addInterface("127.0.0.1").addInterface("127.0.0.1");
``` 
`network.setPortCount:` Uygulamamız başladığı zaman kaç tane port açabilceğini buradan belirliyoruz. Burayı setlemezsek default olarak 100 port açabilecektir. Default port ise 5701’dir.
`join.getMulticasting().setEnabled:`
`join.getTcpIpConfig().addMember:` Burada birden fazla olan iplerimiz arasında bağlantı kurarak bunlar arasında dinleme yapar.
`join.getTcpIpConfig().setEnabled:` ipler arasında joinleme yapılıp yapılmayacağını buradan setliyoruz.
`network.getInterfaces().addInterface:` Burada uygulama başlamadan önce ip doğrulaması yapılması istenildiğinde yanlış ip verilmiş ise uygulama başlatılmayacaktır.
`network.getInterfaces().setEnabled:` interfacenin etkinleştirilip etkinleştirilmediğini kontrol eder.

## _Projenin çalıştırılması_
Öncelikle projemizin  jarını oluşturmak için mvn clean install komutunu kullanıyoruz. Daha sonra aşağıdaki komutla 3 tane farklı portta (8081, 8082, 8083) 3 tane farklı instance ayağa kaldırıyoruz

```java  
java -Dserver.port=8081 -jar springboot-hazelcast-example-0.0.1-SNAPSHOT.jar
java -Dserver.port=8082 -jar springboot-hazelcast-example-0.0.1-SNAPSHOT.jar
java -Dserver.port=8083 -jar springboot-hazelcast-example-0.0.1-SNAPSHOT.jar
```
Uygulamalar ayağa kalktığında aşağıdaki gibi 3 tane ayrı ayrı instanceler oluştuğunu görmüş olduk

![](https://github.com/erogluhasan/springboot-hazelcast-example/blob/master/src/main/resources/1.png?raw=true)

![](https://github.com/erogluhasan/springboot-hazelcast-example/blob/master/src/main/resources/2.png?raw=true)

![](https://github.com/erogluhasan/springboot-hazelcast-example/blob/master/src/main/resources/3.png?raw=true)

Uygulamanın tüm kodlarına şuradan ulaşabilirsiniz: [Github](https://github.com/erogluhasan/springboot-hazelcast-example)

 
