package eu.execom.dry.petclinic.api

case class SearchResultDto[T](results: List[T], totalCount: Int)

object SearchResultDto {
  val RESULTS: String = "results"
  val TOTALCOUNT: String = "totalCount"
}
