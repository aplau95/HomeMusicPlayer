import datetime
import jwt

secret = """-----BEGIN PRIVATE KEY-----
MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQga7dwFxvypB5Oh6qHtwXoBC+gdEmZu/JEEICgd5zzPhegCgYIKoZIzj0DAQehRANCAARunZZqSiZK24eaO14FD0phsA5NYj3UL7zgjjFSyDe08oJhG9mHMzcUcsRN1HsztuwryWn0WDvLV9Mrt+bwnap1
-----END PRIVATE KEY-----"""
teamId = "4N3STV755W";
keyId = "8PF826UBVN"
alg = 'ES256'

time_now = datetime.datetime.now()
time_expired = datetime.datetime.now() + datetime.timedelta(hours=12)

headers = {
    "alg": alg,
    "kid": keyId
}

payload = {
    "iss": teamId,
    "exp": int(time_expired.timestamp()),
    "iat": int(time_now.timestamp())
}


if __name__ == "__main__":
    """Create an auth token"""
    token = jwt.encode(payload, secret, algorithm=alg, headers=headers)

    print("----TOKEN----")
    print(token)

    print("----RESULT----")
    print("curl -v -H 'Authorization: Bearer %s' \"https://api.music.apple.com/v1/catalog/us/artists/36954\" " % (token))