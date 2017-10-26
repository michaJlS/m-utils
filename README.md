# m-utils

[![Build Status](https://travis-ci.org/michaJlS/m-utils.svg?branch=master)](https://travis-ci.org/michaJlS/m-utils)

Pieces of code that show chance of being reused, but are too small to
deserve a separate repo.

- [dict](#dict)

## dict
Simple dictionary module supporting parametrization and randomization of messages,
shipped with json decoder for [circe][circe].

Sample usage:
```Scala
import com.alerf.mutils.dict._
import scala.util.Random

implicit val randGen = new Random()

// Creation
val dict = Dict.fromEntries(
    "key1" -> Multi(Vector("value-1-1", "value-1-2", "value-1-3"))(randGen),
    "key2" -> Single("value-2"),
    // randGen will be provided implicitly
    "key3" -> Multi(Vector("value-3-1"))
    "key10" -> Single("xxx $b$ $a$ yyy $b$"
  )

// Accessing entries
dict("key2") // returns "value-2"
dict.entry("key2") // returns Some("value-2")
dict("key4") // returns "key4"
dict.entry("key4") // returns None
dict("key1") // returns one of values: "value-1-1", "value-1-2", "value-1-3"
dict("key10", "a" -> "100", "b" -> "200") // returns "xxx 200 100 yyy 200"

// Using constructor
val dict1 = new Dict(
    Map(
      "key100" -> Single("value100"),
      "key101" -> Multi(Vector("value101-1", "value102-2")),
      "key103" -> Single("value with a #param#")
    ),
    "#" // using # instead of default $ sign
  )
```


To load dictionary from JSON file (resource):
```Scala
import io.circe.parser.decode
import com.alerf.mutils.dict.Dict
import com.alerf.mutils.dict.marshalling.JsonCirce._

// alias for Either
val dictionary: io.circe.Decoder.Result[Dict] = decode[Dict](Source.fromResource("dictionary.json").mkString)
```

Sample JSON dictionary file:
```json
{
    "message": {
        "error": {
            "not-found": "File not found",
            "access-denied": "You shall not pass!"
        },
        "welcome": [
            "Welcome $username$, how are you?",
            "Nice to see you again, $username$!"
        ]
    }
}
```

It will be resolved as a collection with 3 entries having the following keys:
```
message.error.not-found
message.error.access-denied
message.welcome
```
The `message.welcome` has one parameter named `username`.


Format remarks:
- Use arrays if you want to have multiple values assigned to one key and
  one of them picked randomly by the `Dict`.
- For single value you can use a string, there is no need of using
  an `array` then.
- The key of the particular entry in dict is composed of all keys from the
  JSON root to that element, separated by dots. In this way you can have
  a hierarchy of entries and shorter keys in the JSON file.



[circe]: https://github.com/circe/circe

