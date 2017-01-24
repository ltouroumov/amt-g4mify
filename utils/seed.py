import requests

base_url = "http://localhost:8080"
headers = {}


def post(url, *args, **kwargs):
    return requests.post(base_url + url, headers=headers, *args, **kwargs)


def get(url, *args, **kwargs):
    return requests.get(base_url + url, headers=headers, *args, **kwargs)


def create(what, query):
    print(what, end='')
    resp = query()
    if resp.status_code == 201:
        print(" [OK]")
    else:
        print(" [ERR]", resp.status_code)

resp = post('/register', json = { "name": "BeepBoop" }).json()

print("Connected to domain %s (%d:%s)" % (resp['name'], resp['id'], resp['key']))
headers['Identity'] = "%d:%s" % (resp['id'], resp['key'])

print("Creating counters")
counters = ['beeps', 'boops']

for counter in counters:
    create("counter %s" % counter, lambda: post('/api/counters', json={ "name": counter }))

print("Creating event-rules")
event_rules = [
    { "types": ["beep"], "script": "update 'beeps' add 1" },
    { "types": ["boop"], "script": "update 'boops' add 1" }
]

for event_rule in event_rules:
    create("event-rule", lambda: post('/api/event-rules', json=event_rule))

print("Creating badge-types")
badge_types = [
    { "key": "bronze-beep", "name": "Bronze Beeps", "color": "bronze", "isSingleton": True },
    { "key": "silver-beep", "name": "Silver Beeps", "color": "silver", "isSingleton": True },
    { "key": "gold-beep", "name": "Gold Beeps", "color": "gold", "isSingleton": True },
    { "key": "platinium-beep", "name": "Platinium Beeps", "color": "silver", "isSingleton": True },

    { "key": "bronze-boop", "name": "Bronze Boops", "color": "bronze", "isSingleton": True },
    { "key": "silver-boop", "name": "Silver Boops", "color": "silver", "isSingleton": True },
    { "key": "gold-boop", "name": "Gold Boops", "color": "gold", "isSingleton": True },
    { "key": "platinium-boop", "name": "Platinium Boops", "color": "silver", "isSingleton": True }
]

for badge_type in badge_types:
    create("badge-type %s" % badge_type['key'], lambda: post('/api/badge-types', json=badge_type))

print("Creating badge-rules")
badge_rules = [
    { "condition": "when 'beeps' matches { it >= 10 }", "grants": "bronze-beep" },
    { "condition": "when 'beeps' matches { it >= 100 }", "grants": "silver-beep" },
    { "condition": "when 'beeps' matches { it >= 1000 }", "grants": "gold-beep" },
    { "condition": "when 'beeps' matches { it >= 10000 }", "grants": "platinium-beep" },

    { "condition": "when 'boops' matches { it >= 10 }", "grants": "bronze-boop" },
    { "condition": "when 'boops' matches { it >= 100 }", "grants": "silver-boop" },
    { "condition": "when 'boops' matches { it >= 1000 }", "grants": "gold-boop" },
    { "condition": "when 'boops' matches { it >= 10000 }", "grants": "platinium-boop" },
]

for badge_rule in badge_rules:
    create("badge-rule", lambda: post('/api/badge-rules', json=badge_rule))

