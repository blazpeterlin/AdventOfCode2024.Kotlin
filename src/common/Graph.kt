package common

import java.util.PriorityQueue

data class GraphNode<T>(val elt: T, val score: Long)
data class PathBack<T>(val elt: T, val score: Long, val prevPath: PathBack<T>?)

class Graph<T> {
    public fun Traverse(starts: List<T>, getNeighboursCosts: (T)->List<Pair<T, Long>>, getScore: (T)->Long): Sequence<T> {
        val pq = PriorityQueue<GraphNode<T>>(compareBy { it.score })
        pq.addAll(starts.map{ s -> GraphNode(s, getScore(s))})

        val visited = HashSet<T>()

        return sequence {
            while(pq.any()) {
                val node = pq.poll()
                if (visited.contains(node.elt)) { continue }

                yield(node.elt)

                visited.add(node.elt)

                val ns = getNeighboursCosts(node.elt).filter{ (n, cost) -> !visited.contains(n) }

                pq.addAll(ns.map{ (n, cost) -> GraphNode(n, getScore(n))})
            }
        }
    }

    public fun<U> TraverseWithIds(starts: List<T>, getNeighbours: (T)->List<T>, getScore: (T)->Long, getId: (T)->U): Sequence<T> {
        val pq = PriorityQueue<GraphNode<T>>(compareBy { it.score })
        pq.addAll(starts.map{ s -> GraphNode(s, getScore(s))})

        val visited = HashSet<U>()

        return sequence {
            while(pq.any()) {
                val node = pq.poll()
                if (visited.contains(getId(node.elt))) { continue }

                yield(node.elt)

                visited.add(getId(node.elt))

                val ns = getNeighbours(node.elt).filter{ n -> !visited.contains(getId(n)) }

                pq.addAll(ns.map{ n -> GraphNode(n, getScore(n))})
            }
        }
    }

    public fun TraverseWithPaths(starts: List<T>, getNeighboursCosts: (T)->List<Pair<T, Long>>, getScore: (T)->Long): Sequence<PathBack<T>> {
        val pq = PriorityQueue<PathBack<T>>(compareBy { getScore(it.elt) })
        pq.addAll(starts.map{ PathBack(it, getScore(it), null)})

        val visited = HashSet<T>()

        return sequence {
            while(pq.any()) {
                val node = pq.poll()
                if (visited.contains(node.elt)) { continue }

                yield(node)

                visited.add(node.elt)

                val ns = getNeighboursCosts(node.elt).filter{ (n, cost) -> !visited.contains(n) }

                pq.addAll(ns.map{ (n, cost) -> PathBack(n, getScore(n), node)})
            }
        }
    }
}