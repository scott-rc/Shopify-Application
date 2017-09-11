(ns shopify.util.atom)

(defn inc!! [a]
  (reset! a (inc @a)))

(defn assoc!!
  ([a k v]
   (swap! a assoc k v))
  ([a k v & kvs]
   (let [ret (assoc!! a k v)]
     (if kvs
       (if (next kvs)
         (recur a (first kvs) (second kvs) (nnext kvs)))
       ret))))

(defn conj!!
  ([a x]
   (swap! a conj x))
  ([a x & xs]
   (let [ret (conj!! a x)]
     (if xs
       (recur a (first xs) (rest xs))
       ret))))
