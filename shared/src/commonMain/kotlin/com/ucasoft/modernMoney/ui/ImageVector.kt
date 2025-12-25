package com.ucasoft.modernMoney.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.toPath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

fun ImageVector.toImageBitmap(
    density: Density = Density(1f),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    size: Size = Size(defaultWidth.value * density.density, defaultHeight.value * density.density)
): ImageBitmap {
    val imageBitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(imageBitmap)
    val scope = CanvasDrawScope()

    scope.draw(
        density = density,
        layoutDirection = layoutDirection,
        canvas = canvas,
        size = size
    ) {
        val scaleX = size.width / viewportWidth
        val scaleY = size.height / viewportHeight

        withTransform({
            scale(scaleX, scaleY, Offset.Companion.Zero)
        }) {
            drawVectorGroup(root)
        }
    }
    return imageBitmap
}

private fun DrawScope.drawVectorGroup(group: VectorGroup) {
    withTransform({
        translate(group.translationX, group.translationY)
        rotate(group.rotation, Offset(group.pivotX, group.pivotY))
        scale(group.scaleX, group.scaleY, Offset(group.pivotX, group.pivotY))
        if (group.clipPathData.isNotEmpty()) {
            val clipPath = Path()
            group.clipPathData.toPath(clipPath)
            clipPath(clipPath)
        }
    }) {
        group.forEach { node ->
            when (node) {
                is VectorGroup -> drawVectorGroup(node)
                is VectorPath -> drawVectorPath(node)
            }
        }
    }
}

private fun DrawScope.drawVectorPath(node: VectorPath) {
    val path = Path()
    node.pathData.toPath(path)

    val pathToDraw = if (node.trimPathStart != 0f || node.trimPathEnd != 1f || node.trimPathOffset != 0f) {
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        val length = pathMeasure.length
        val start = ((node.trimPathStart + node.trimPathOffset) % 1f) * length
        val end = ((node.trimPathEnd + node.trimPathOffset) % 1f) * length

        val newPath = Path()
        if (start > end) {
            pathMeasure.getSegment(start, length, newPath, true)
            pathMeasure.getSegment(0f, end, newPath, true)
        } else {
            pathMeasure.getSegment(start, end, newPath, true)
        }
        newPath
    } else {
        path
    }

    node.fill?.let { brush ->
        drawPath(
            path = pathToDraw,
            brush = brush,
            alpha = node.fillAlpha
        )
    }

    node.stroke?.let { brush ->
        drawPath(
            path = pathToDraw,
            brush = brush,
            alpha = node.strokeAlpha,
            style = Stroke(
                width = node.strokeLineWidth,
                cap = node.strokeLineCap,
                join = node.strokeLineJoin,
                miter = node.strokeLineMiter
            )
        )
    }
}