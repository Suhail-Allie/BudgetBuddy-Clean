package com.example.budgetbuddy

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvGoals: TextView
    private lateinit var tvCategoryCount: TextView
    private lateinit var tvTransactions: TextView
    private lateinit var tvMonthlySpent: TextView
    private lateinit var tvBudgetStatus: TextView
    private lateinit var tvBudgetPercent: TextView
    private lateinit var progressBudget: ProgressBar
    private lateinit var tvBadges: TextView

    private lateinit var etAmount: EditText
    private lateinit var btnAddIncome: Button
    private lateinit var btnAddExpense: Button
    private lateinit var btnClearHistory: Button
    private lateinit var btnLogout: Button

    private lateinit var btnManageCategories: Button
    private lateinit var btnBudgetGoals: Button
    private lateinit var btnFilter: Button
    private lateinit var btnViewReports: Button

    private lateinit var dao: ExpenseDao

    private lateinit var currentUserKey: String
    private lateinit var currentDisplayName: String
    private lateinit var userPrefs: SharedPreferences

    private val transactionList = mutableListOf<String>()
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private var activeReceiptPreview: ImageView? = null
    private var activeReceiptPath: String? = null

    private val receiptPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) {
            Toast.makeText(this, "No receipt selected", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }

        try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (exception: SecurityException) {
            Log.w("Main", "Could not persist receipt URI permission", exception)
        }

        activeReceiptPath = uri.toString()

        activeReceiptPreview?.apply {
            visibility = View.VISIBLE
            setImageURI(uri)
        }

        Toast.makeText(this, "Receipt attached", Toast.LENGTH_SHORT).show()
        Log.d("Main", "Receipt selected for $currentUserKey: $uri")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUserKey = getCurrentUserKey()
        currentDisplayName = getCurrentDisplayName()
        userPrefs = getUserPrefs(currentUserKey)

        setContentView(R.layout.activity_main)

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser)
        tvBalance = findViewById(R.id.tvBalance)
        tvGoals = findViewById(R.id.tvGoals)
        tvCategoryCount = findViewById(R.id.tvCategoryCount)
        tvTransactions = findViewById(R.id.tvTransactions)
        tvMonthlySpent = findViewById(R.id.tvMonthlySpent)
        tvBudgetStatus = findViewById(R.id.tvBudgetStatus)
        tvBudgetPercent = findViewById(R.id.tvBudgetPercent)
        progressBudget = findViewById(R.id.progressBudget)
        tvBadges = findViewById(R.id.tvBadges)

        etAmount = findViewById(R.id.etAmount)
        btnAddIncome = findViewById(R.id.btnAddIncome)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        btnLogout = findViewById(R.id.btnLogout)

        btnManageCategories = findViewById(R.id.btnManageCategories)
        btnBudgetGoals = findViewById(R.id.btnBudgetGoals)
        btnFilter = findViewById(R.id.btnFilter)
        btnViewReports = findViewById(R.id.btnViewReports)

        dao = AppDatabase.getDatabase(this).expenseDao()

        loadUserData()
        observeDashboardData()

        btnAddIncome.setOnClickListener {
            val amount = getAmountInput() ?: return@setOnClickListener

            transactionList.add("Income: +R${formatMoney(amount)}")

            saveExpense(
                amount = amount,
                category = "Income",
                type = "income",
                title = "Income",
                date = System.currentTimeMillis(),
                receiptPhotoPath = null
            )

            updateTransactionPreview()
            etAmount.text.clear()

            Log.d("Main", "Income saved for userKey=$currentUserKey amount=$amount")
        }

        btnAddExpense.setOnClickListener {
            showAddExpenseDialog()
        }

        btnClearHistory.setOnClickListener {
            transactionList.clear()
            updateTransactionPreview()

            Toast.makeText(
                this,
                "Screen history cleared. Saved data remains in Reports.",
                Toast.LENGTH_LONG
            ).show()

            Log.d("Main", "Screen transaction preview cleared for $currentUserKey")
        }

        btnLogout.setOnClickListener {
            getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnManageCategories.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        btnBudgetGoals.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }

        btnFilter.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        btnViewReports.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        currentUserKey = getCurrentUserKey()
        currentDisplayName = getCurrentDisplayName()
        userPrefs = getUserPrefs(currentUserKey)

        loadUserData()
    }

    private fun showAddExpenseDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null)

        val etExpenseTitle = dialogView.findViewById<EditText>(R.id.etExpenseTitle)
        val etExpenseAmount = dialogView.findViewById<EditText>(R.id.etExpenseAmount)
        val etExpenseDate = dialogView.findViewById<EditText>(R.id.etExpenseDate)
        val spExpenseCategory = dialogView.findViewById<Spinner>(R.id.spExpenseCategory)
        val etExpenseNotes = dialogView.findViewById<EditText>(R.id.etExpenseNotes)
        val btnAttachReceipt = dialogView.findViewById<Button>(R.id.btnTakePhoto)
        val ivReceiptPreview = dialogView.findViewById<ImageView>(R.id.ivReceiptPreview)

        val quickAmount = etAmount.text.toString().trim()
        if (quickAmount.isNotEmpty()) {
            etExpenseAmount.setText(quickAmount)
        }

        val categories = loadCategories()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        spExpenseCategory.adapter = adapter

        val selectedDate = Calendar.getInstance()
        etExpenseDate.setText(dateFormatter.format(selectedDate.time))

        etExpenseDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    selectedDate.set(Calendar.HOUR_OF_DAY, 12)
                    selectedDate.set(Calendar.MINUTE, 0)
                    selectedDate.set(Calendar.SECOND, 0)
                    selectedDate.set(Calendar.MILLISECOND, 0)

                    etExpenseDate.setText(dateFormatter.format(selectedDate.time))
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        activeReceiptPath = null
        activeReceiptPreview = ivReceiptPreview

        btnAttachReceipt.setOnClickListener {
            receiptPickerLauncher.launch(arrayOf("image/*"))
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Expense Details")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { cancelDialog, _ ->
                activeReceiptPreview = null
                activeReceiptPath = null
                cancelDialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val title = etExpenseTitle.text.toString().trim()
                val amountText = etExpenseAmount.text.toString().trim()
                val notes = etExpenseNotes.text.toString().trim()
                val category = spExpenseCategory.selectedItem?.toString() ?: "General"

                if (title.isEmpty()) {
                    etExpenseTitle.error = "Expense description is required"
                    etExpenseTitle.requestFocus()
                    return@setOnClickListener
                }

                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0.0) {
                    etExpenseAmount.error = "Enter a valid amount greater than 0"
                    etExpenseAmount.requestFocus()
                    return@setOnClickListener
                }

                val fullTitle = if (notes.isEmpty()) {
                    title
                } else {
                    "$title - $notes"
                }

                transactionList.add("Expense: -R${formatMoney(amount)} | $category | $title")

                saveExpense(
                    amount = amount,
                    category = category,
                    type = "expense",
                    title = fullTitle,
                    date = selectedDate.timeInMillis,
                    receiptPhotoPath = activeReceiptPath
                )

                updateTransactionPreview()
                etAmount.text.clear()

                activeReceiptPreview = null
                activeReceiptPath = null

                Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show()
                Log.d("Main", "Expense saved for userKey=$currentUserKey amount=$amount category=$category")

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun observeDashboardData() {
        lifecycleScope.launch {
            dao.getExpensesForUser(currentUserKey).collect { expenses ->
                val minGoal = userPrefs.getFloat("minGoal", 0f)
                val maxGoal = userPrefs.getFloat("maxGoal", 0f)

                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                val balance = BudgetBuddyLogic.calculateBalance(expenses)
                val monthlySpent = BudgetBuddyLogic.monthlySpending(expenses, month, year)
                val progress = BudgetBuddyLogic.progressPercent(monthlySpent, maxGoal)
                val status = BudgetBuddyLogic.budgetStatus(monthlySpent, minGoal, maxGoal)
                val badges = BudgetBuddyLogic.earnedBadges(
                    expenses,
                    monthlySpent,
                    minGoal,
                    maxGoal
                )

                tvBalance.text = "Balance: R${formatMoney(balance)}"
                tvMonthlySpent.text = "This month spent: R${formatMoney(monthlySpent)}"

                tvBudgetPercent.text = if (maxGoal > 0f) {
                    "$progress% of maximum monthly goal used"
                } else {
                    "Set a maximum goal to activate the progress bar"
                }

                progressBudget.progress = progress
                tvBudgetStatus.text = status

                tvBadges.text = if (badges.isEmpty()) {
                    "Badges: Add expenses and stay within your goals to unlock rewards."
                } else {
                    "Badges: ${badges.joinToString("  ")}"
                }

                Log.d(
                    "UserCheck",
                    "Dashboard loaded for userKey=$currentUserKey displayName=$currentDisplayName expenses=${expenses.size}"
                )
            }
        }
    }

    private fun loadUserData() {
        val minGoal = userPrefs.getFloat("minGoal", 0f)
        val maxGoal = userPrefs.getFloat("maxGoal", 0f)
        val categoryCount = loadCategories().size

        tvWelcomeUser.text = "Welcome, $currentDisplayName"

        tvGoals.text = if (minGoal <= 0f && maxGoal <= 0f) {
            "Goals: Not set"
        } else {
            "Goals: Min R${formatMoney(minGoal.toDouble())} | Max R${formatMoney(maxGoal.toDouble())}"
        }

        tvCategoryCount.text = "Categories available: $categoryCount"

        Log.d(
            "UserCheck",
            "loadUserData userKey=$currentUserKey displayName=$currentDisplayName min=$minGoal max=$maxGoal categories=$categoryCount"
        )
    }

    private fun saveExpense(
        amount: Double,
        category: String,
        type: String,
        title: String,
        date: Long,
        receiptPhotoPath: String?
    ) {
        lifecycleScope.launch {
            dao.insertExpense(
                Expense(
                    userId = currentUserKey,
                    title = title,
                    amount = amount,
                    category = category,
                    type = type,
                    date = date,
                    receiptPhotoPath = receiptPhotoPath
                )
            )
        }
    }

    private fun getAmountInput(): Double? {
        val input = etAmount.text.toString().trim()

        if (input.isEmpty()) {
            etAmount.error = "Enter amount"
            etAmount.requestFocus()
            return null
        }

        val amount = input.toDoubleOrNull()

        if (amount == null || amount <= 0.0) {
            etAmount.error = "Enter a valid amount greater than 0"
            etAmount.requestFocus()
            return null
        }

        return amount
    }

    private fun updateTransactionPreview() {
        tvTransactions.text = if (transactionList.isEmpty()) {
            "No transactions added in this session yet"
        } else {
            transactionList.takeLast(5).joinToString("\n")
        }
    }

    private fun loadCategories(): List<String> {
        val categoryText = userPrefs.getString("categories", "") ?: ""

        val savedCategories = categoryText
            .lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        return if (savedCategories.isEmpty()) {
            listOf("General")
        } else {
            savedCategories.distinct()
        }
    }

    private fun getCurrentUserKey(): String {
        val sessionPrefs = getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
        val userKey = sessionPrefs.getString("currentUserKey", null)

        return if (!userKey.isNullOrBlank()) {
            userKey.trim().lowercase()
        } else {
            "guest"
        }
    }

    private fun getCurrentDisplayName(): String {
        val sessionPrefs = getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
        val displayName = sessionPrefs.getString("currentUsername", null)

        return if (!displayName.isNullOrBlank()) {
            displayName.trim()
        } else {
            "Guest"
        }
    }

    private fun getUserPrefs(userKey: String): SharedPreferences {
        return getSharedPreferences("UserData_${userKey.lowercase()}", MODE_PRIVATE)
    }

    private fun formatMoney(value: Double): String {
        return "%.2f".format(value)
    }
}