import {
    getAvailableTimeZoneIDs,
    getCurrentDateTime, getCurrentTimestamp, getCurrentTimeZoneId,
    getTimeZoneOffset } from 'libentry.so';
import { LengthMetrics } from '@kit.ArkUI';

const DOMAIN = 0x0000;

@Entry
@ComponentV2
struct Index {

    build() {
        Row() {
            Column() {
                Text(getCurrentTimeZoneId())
                Text(getCurrentDateTime())
                Text(getCurrentTimestamp().toString())
                Text(getTimeZoneOffset().toString())

                Stack().height(24)

                List() {

                    Repeat(getAvailableTimeZoneIDs().split(","))
                        .virtualScroll()
                        .each(it => {

                            ListItem() {
                                Text(it.item.trim())
                            }.alignSelf(ItemAlign.Center)
                        })

                }.width('100%')
                .layoutWeight(1)
            }
            .width('100%')
        }
        .height('100%')
        .safeAreaPadding(LengthMetrics.vp(0))
    }
}
