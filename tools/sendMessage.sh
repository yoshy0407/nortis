#!/bin/bash
apiKey=$1
flg=$2
repository=$3
url=$4

if [ "${flg}" = "success" ]; then
    message="${repository} is build success"
    stickerId="52002734"
else
    message="${repository} is build failure"
    stickerId="52002744"
fi

cat << EOF > body.json
{
    "messages":[
        {
            "type":"text",
            "text":"${message}"
        },
        {
            "type":"text",
            "text":"${url}"
        },
        {
            "type":"sticker",
            "packageId":"11537",
            "stickerId":"${stickerId}"
        }
    ]
}
EOF

curl -X POST https://api.line.me/v2/bot/message/broadcast \
-H 'Content-Type: application/json' \
-H "Authorization: Bearer $apiKey" \
-H 'X-Line-Retry-Key: ' \
-d @body.json

rm body.json