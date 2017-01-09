# Rules Engine

G4mify uses [Groovy](http://www.groovy-lang.org/) as a scripting engine to execute rules.

There are two types of rules:

  1. Event Rules, which trigger when processing incoming events and update the state of the system by updating counters and granting badges.
  2. Badge Rules, which trigger when counters are updated and grant badges when certain conditions are met.

## Event Rules

An event rule processes one or more event types. The API provides two functions that can be used in the script to affect the environment.

### `update $counter (add|sub|set) $amount`

  * `$counter` is a counter reference (in the form of `counter-name(.metric-name)?`)
  * `$amount` is an integer

Modifies a counter's value.

```groovy
update 'counter' add 10
update 'counter' sub 10
update 'counter' set 10
```

### `award $badge-type`

  * `$badge-type` is the name of a badge type

Awards a badge to the user

*Examples*
```groovy
award 'silver-medal'
award 'gold-medal'
award 'participation-medal'
```

## Badge Rules

Badge rules use a [DSL](https://en.wikipedia.org/wiki/Domain-specific_language) to check counter values. The API provides a construct to match a counter value against a predicate and two constructs to combine them by logic operators.

### `when $counter matches $predicate`

  * `$predicate` is a [groovy closure](http://www.groovy-lang.org/closures.html) returning a `boolean` value.
  * `$counter` is a counter reference (in the form of `counter-name(.metric-name)?`)

This will match the value of `$counter` against `$predicate`

### `and { ... }`

Combines the result of the expressions contained in `{ ... }` with the `AND` logical operator.

### `or { ... }`

Combines the result of the expressions contained in `{ ... }` with the `OR` logical operator.