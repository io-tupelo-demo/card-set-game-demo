(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [schema.core :as s]
    [tupelo.schema :as tsk]))

(dotest
  (throws-not? (s/validate Color :purple))
  (throws-not? (s/validate Color :red))
  (throws-not? (s/validate Color :green))
  (throws? (s/validate Color :blue)))

(def card-11
  {:color   :purple
   :number  3
   :shading :full
   :shape   :diamond})

(def card-12
  {:color   :red
   :number  3
   :shading :lines
   :shape   :diamond})

(def card-13
  {:color   :green
   :number  3
   :shading :empty
   :shape   :diamond})

(def card-21
  {:color   :purple
   :number  3
   :shading :full
   :shape   :diamond})

(def card-22
  {:color   :red
   :number  3
   :shading :lines
   :shape   :diamond})

(def card-23
  {:color   :purple
   :number  3
   :shading :empty
   :shape   :diamond})

(def hand-1 [card-11 card-12 card-13])
(def hand-2 [card-21 card-22 card-23])

(dotest
  (is (s/validate Card card-11))
  (is (s/validate Card card-12))
  (is (s/validate Card card-13))
  (is (s/validate Card card-21))
  (is (s/validate Card card-22))
  (is (s/validate Card card-23)))


(def cum-11 {:color   #{:purple}
             :number  #{3}
             :shading #{:full}
             :shape   #{:diamond}})

(dotest
  (is= cum-11 (accum-card CardAccumulatorEmpty card-11))
  (is= (reduce accum-card CardAccumulatorEmpty hand-1)
    {:color   #{:green :red :purple},
     :number  #{3},
     :shading #{:full :lines :empty},
     :shape   #{:diamond}})
  (is= (reduce accum-card CardAccumulatorEmpty hand-2)
    {:color   #{:red :purple},
     :number  #{3},
     :shading #{:full :lines :empty},
     :shape   #{:diamond}}))

(dotest
  (is= [1 1 1 1] (accum->counts cum-11))
  (is= [2 3 2 1] (accum->counts {:color   #{:purple :red}
                                 :number  #{3 2 1}
                                 :shading #{:full :empty}
                                 :shape   #{:diamond}})))

(dotest
  (is (are-cards-a-set? hand-1))
  (isnt (are-cards-a-set? hand-2)))


