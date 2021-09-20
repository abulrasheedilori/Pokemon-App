package com.apipokemonproject.abilitiesApi

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)