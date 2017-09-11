(ns shopify.util.default)

(def back-end {:json "{\"validations\":[{\"name\":{\"required\":true,\"type\":\"string\",\"length\":{\"min\":5}}},{\"email\":{\"required\":true}},{\"age\":{\"type\":\"number\",\"required\":false}},{\"newsletter\":{\"required\":true,\"type\":\"boolean\"}}],\"customers\":[{\"id\":1,\"name\":\"David\",\"email\":\"david@interview.com\",\"age\":null,\"country\":\"France\",\"newsletter\":true},{\"id\":2,\"name\":\"Lily\",\"email\":\"lily@interview.com\",\"age\":24,\"country\":\"China\",\"newsletter\":false},{\"id\":3,\"name\":\"Bernardo\",\"email\":\"bernardo@interview.com\",\"age\":30,\"country\":\"Brazil\",\"newsletter\":\"false\"},{\"id\":4,\"name\":\"Gabriel\",\"email\":\"gabriel@interview.com\",\"age\":28,\"country\":\"Canada\",\"newsletter\":true},{\"id\":5,\"name\":\"Alex\",\"email\":\"alex@interview.com\",\"age\":29,\"country\":\"United States\",\"newsletter\":true}],\"pagination\":{\"current_page\":1,\"per_page\":5,\"total\":16}}"
               :result "Results will show up here!"})

(def data-engineering {:table1 "[{\"cid\":1,\"name\":\"Barry\"},{\"cid\":2,\"name\":\"Todd\"},{\"cid\":3,\"name\":\"Steve\"},{\"cid\":4,\"name\":\"Edward\"},{\"cid\":5,\"name\":\"Rodney\"}]"
                       :table2 "[{\"oid\":1,\"customer_id\":1,\"price\":2.5},{\"oid\":2,\"customer_id\":3,\"price\":5},{\"oid\":3,\"customer_id\":3,\"price\":2},{\"oid\":4,\"customer_id\":2,\"price\":2},{\"oid\":5,\"customer_id\":6,\"price\":3},{\"oid\":6,\"customer_id\":5,\"price\":4},{\"oid\":7,\"customer_id\":1,\"price\":10}]"
                       :table1-key :cid
                       :table2-key :customer_id
                       :table1-name "customers"
                       :table2-name "orders"
                       :join-type :inner-join
                       :result nil})

