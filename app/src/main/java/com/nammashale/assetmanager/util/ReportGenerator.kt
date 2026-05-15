package com.nammashale.assetmanager.util

import com.nammashale.assetmanager.data.model.Asset
import com.nammashale.assetmanager.data.model.Issue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility for generating summary reports.
 */
object ReportGenerator {

    /**
     * Generates a plain-text summary report of assets and issues.
     */
    fun generateTextReport(assets: List<Asset>, issues: List<Issue>): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val generatedAt = dateFormat.format(Date())

        val totalAssets = assets.size
        val working = assets.count { it.condition == "Working" }
        val needsRepair = assets.count { it.condition == "Needs Repair" }
        val broken = assets.count { it.condition == "Broken" }

        val unresolvedIssues = issues.count { !it.resolved }

        // Category breakdown
        val categoryBreakdown = assets.groupBy { it.category }
            .map { "${it.key}: ${it.value.size}" }
            .joinToString("\n")

        return """
            Namma Shaale - Asset Report
            Generated: $generatedAt
            
            -----------------------------------------
            OVERVIEW
            -----------------------------------------
            Total Assets: $totalAssets
            
            Working: $working
            Needs Repair: $needsRepair
            Broken: $broken
            
            Unresolved Issues: $unresolvedIssues
            
            -----------------------------------------
            CATEGORY BREAKDOWN
            -----------------------------------------
            $categoryBreakdown
            
            -----------------------------------------
            RECENTLY LOGGED ISSUES (Unresolved)
            -----------------------------------------
            ${issues.filter { !it.resolved }.take(5).joinToString("\n") { 
                "- Asset #${it.assetId}: ${it.description}" 
            }.ifEmpty { "None" }}
            
            -----------------------------------------
            End of Report
        """.trimIndent()
    }
}
