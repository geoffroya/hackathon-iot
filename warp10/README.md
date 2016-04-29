WRITE

token=g6qRxCrc2I3zVRYAqRSMlDDcTE_JXdzYTqvAE0g77Yuoi__evW1KbaZVQyTSbiTzwlkD9PNiJybUrGuLevcxFGdKidKU1oRkd4EuEpbBPUDdf1eM.wCrbUqhNwxrPT06
tokenIdent=fd49fb8eac47a169
application name=io.warp10.bootstrap
producer & owner=bb1a5cee-a924-48b9-8642-d394ba29d740 & bb1a5cee-a924-48b9-8642-d394ba29d740
ttl=3600000000000

curl -H 'X-Warp10-Token: g6qRxCrc2I3zVRYAqRSMlDDcTE_JXdzYTqvAE0g77Yuoi__evW1KbaZVQyTSbiTzwlkD9PNiJybUrGuLevcxFGdKidKU1oRkd4EuEpbBPUDdf1eM.wCrbUqhNwxrPT06' --data-binary "1// test{} 42" 'http://127.0.0.1:8080/api/v0/update'


READ


token=rSfyXOPnL7Be0mQoRNZBgm3BdHCdqaLypnpfmRn1KZ.lV3NT56dbx7B9aCONaD1wB.gUsr4FlC3HyByvyQT4LL8eUw1.3k7.T61IjEjpUXyyWaoEk2RsxW0xb7rinevKcZn7hKgwNY1ipqgq3tDPvd.V3g20llCk
tokenIdent=31dda8fbb14a425e
application name=io.warp10.bootstrap
producer & owner=bb1a5cee-a924-48b9-8642-d394ba29d740 & bb1a5cee-a924-48b9-8642-d394ba29d740
ttl=3600000000000

curl --data-binary "'rSfyXOPnL7Be0mQoRNZBgm3BdHCdqaLypnpfmRn1KZ.lV3NT56dbx7B9aCONaD1wB.gUsr4FlC3HyByvyQT4LL8eUw1.3k7.T61IjEjpUXyyWaoEk2RsxW0xb7rinevKcZn7hKgwNY1ipqgq3tDPvd.V3g20llCk' 'test' {} NOW -1 FETCH" 'http://127.0.0.1:8080/api/v0/exec'

DELETE

curl -v -H 'X-Warp10-Token: g6qRxCrc2I3zVRYAqRSMlDDcTE_JXdzYTqvAE0g77Yuoi__evW1KbaZVQyTSbiTzwlkD9PNiJybUrGuLevcxFGdKidKU1oRkd4EuEpbBPUDdf1eM.wCrbUqhNwxrPT06' 'http://192.168.56.6:8080/api/v0/delete?deleteall&selector=hackathon\{\}'



Fetch de la dernière minute ?
Moyenne => [ SWAP bucketizer.mean 0 0 1 ] BUCKETIZE
