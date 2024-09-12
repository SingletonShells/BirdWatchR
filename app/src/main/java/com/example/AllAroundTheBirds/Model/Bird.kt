package com.example.AllAroundTheBirds.Model

data class Bird(val name: String?, val species: String?, val imageUri: String?)
{
    // Add a default (no-argument) constructor
    constructor() : this("", "", "")
}

