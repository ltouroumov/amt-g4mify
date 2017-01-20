import requests, sys, random, time, argparse

parser = argparse.ArgumentParser()
parser.add_argument('identity')
parser.add_argument('count', default=10, type=int)
parser.add_argument('--delay', default=0.2, type=float)

args = parser.parse_args()

base_url = "http://localhost:8080"
headers = {
    'Identity': args.identity
}


def post(url, *args, **kwargs):
    return requests.post(base_url + url, headers=headers, *args, **kwargs)


def get(url, *args, **kwargs):
    return requests.get(base_url + url, headers=headers, *args, **kwargs)

resp = get('/api/domain')
if resp.status_code != 200:
    print("Identity invalid", resp.status_code)
    sys.exit()

resp = resp.json()
print("Connected to domain %s (%d)" % (resp['name'], resp['id']))

print("Bombarding ...")
events = ['beep', 'boop']
users = ['foo', 'bar', 'baz']

for qid in range(args.count):
    user = random.choice(users)
    event = random.choice(events)
    print("[%03d] %s does %s" % (qid, user, event), end='')
    resp = post('/api/events', json={
        "user": user,
        "type": event,
        "data": {}
    })
    if resp.status_code == 200:
        print("[OK]")
        time.sleep(args.delay)
    else:
        print("[ERR]")
        print(resp.json())
        break
