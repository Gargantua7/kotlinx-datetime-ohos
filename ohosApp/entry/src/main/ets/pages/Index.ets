import testNapi, { getCurrentDateTime, getCurrentTimestamp, getCurrentTimeZoneId,
    getTimeZoneOffset } from 'libentry.so';

const DOMAIN = 0x0000;

@Entry
@Component
struct Index {
    @State message: string = 'Hello World';



    build() {
        Row() {
            Column() {
                Text(getCurrentTimeZoneId())
                Text(getCurrentDateTime())
                Text(getCurrentTimestamp().toString())
                Text(getTimeZoneOffset().toString())
            }
            .width('100%')
        }
        .height('100%')
    }
}
