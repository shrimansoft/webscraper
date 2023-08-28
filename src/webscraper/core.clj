(ns webscraper.core
  (:gen-class)
  (:require [reaver :as rv])
  )

(def hacker-news (slurp "https://news.ycombinator.com/"))

;; Extract the headlines and urls from the HTML into a seq of maps.


;; Example 0
;; (rv/extract (rv/parse hacker-news) [] "" rv/edn)

;; Example 1
;; (rv/extract (rv/parse hacker-news) [] ".athing  .title > span > a" rv/edn)

;; Example 2
;; (rv/extract (rv/parse hacker-news) [] ".titleline " (rv/attr :href))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println

   (rv/extract-from (rv/parse hacker-news) ".itemlist .athing"
                    [:headline :url]
                    ".title > a" rv/text
                    ".title > a" (rv/attr :href))

   )

  (println (rv/parse hacker-news))

  (rv/parse hacker-news)




  )
