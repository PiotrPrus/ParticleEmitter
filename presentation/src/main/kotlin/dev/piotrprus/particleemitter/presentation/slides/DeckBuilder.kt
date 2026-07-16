package dev.piotrprus.particleemitter.presentation.slides

import dev.piotrprus.particleemitter.presentation.deck.Slide

fun deckSlides(): List<Slide> = listOf(
    titleSlide,
    whatIsItSlide,
    layoutEngineSlide,
    canvasEngineSlide,
    frameLoopSlide,
    physicsSlide,
    gravityRealismSlide,
    gravitySlide,
    emissionSlide,
    edgeBehaviorSlide,
    performanceSlide,
    // add the dependency
    dependencySlide,
    // your first emitter
//    firstEmitterSlide,
    // flame, matrix, wand
    inspirationSlide,
    // star wars slide (add my photo there)
    starWarsSlide,
    // the one that reveal this is the desktop app (add Marton)
    multiplatformSlide,
    // Your turn: scan & go
    setupSlide,
    closingSlide,
)
