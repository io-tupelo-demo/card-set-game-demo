(ns demo.core
  (:use tupelo.core)
  (:require
    [schema.core :as s]
    [tupelo.schema :as tsk]))

(def Color (s/enum :purple :red :green))
(def CardNumber (s/enum 1 2 3))
(def Shading (s/enum :empty :lines :full))
(def Shape (s/enum :Squiggle :oval :diamond))

(def Card {:color   Color
           :number  CardNumber
           :shading Shading
           :shape   Shape})

(def CardProp (apply s/enum (keys Card)))

(def CardAccumulator {:color   #{Color}
                      :number  #{CardNumber}
                      :shading #{Shading}
                      :shape   #{Shape}})

(def CardAccumulatorEmpty {:color   #{}
                           :number  #{}
                           :shading #{}
                           :shape   #{}})

(s/defn accum-card
  [accum :- CardAccumulator
   card :- Card]
  (reduce
    (s/fn [accum :- CardAccumulator
           card-mapentry :- tsk/MapEntry]
      (let [[cardprop propval] card-mapentry]
        (update-in accum [cardprop]
          conj propval)))
    accum
    card))

(s/defn accum->counts :- [s/Num]
  [accum :- CardAccumulator]
  (mapv count (vals accum)))

(s/defn are-cards-a-set? :- s/Bool
  [cards :- [Card]]
  (let [cum-cards        (reduce accum-card CardAccumulatorEmpty cards)
        cum-cards-counts (accum->counts cum-cards)
        valid-set-count? (s/fn [n :- Number] (or (= n 1) (= n 3)))
        result           (every? valid-set-count? cum-cards-counts)]
    result))



#_(defn -main
    [& args]
    )

