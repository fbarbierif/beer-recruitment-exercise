package com.example.beerrecruitmentexercise.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.beerrecruitmentexercise.R
import com.example.beerrecruitmentexercise.ui.adapter.BeersAdapter
import com.example.beerrecruitmentexercise.dto.BeerDTO
import com.example.beerrecruitmentexercise.presenter.BeersPresenter
import com.example.beerrecruitmentexercise.view.BeersView

import java.util.ArrayList
import java.util.Comparator

import com.example.beerrecruitmentexercise.utils.RealmUtils.deleteFromRealmWithoutKey
import com.example.beerrecruitmentexercise.utils.RealmUtils.isSearchStoredInDB
import com.example.beerrecruitmentexercise.utils.RealmUtils.restoreSearchFromDB
import com.example.beerrecruitmentexercise.utils.RealmUtils.storeBeers

class BeersListActivity : AppCompatActivity(), BeersView {

    private var beersPresenter: BeersPresenter? = null
    private var beersAdapter: BeersAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private var progressBar: ProgressBar? = null
    private var llErrorEmptyView: LinearLayout? = null
    private var tvMessage: TextView? = null
    private var srLayout: SwipeRefreshLayout? = null
    private lateinit var etSearch: EditText
    private lateinit var ivClear: ImageView
    private var beers = ArrayList<BeerDTO>()
    private var food: String? = null
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beers_list)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rvBeers)
        etSearch = findViewById(R.id.etSearch)
        ivClear = findViewById(R.id.ivClear)
        llErrorEmptyView = findViewById(R.id.llErrorEmptyView)
        tvMessage = findViewById(R.id.tvMessage)
        srLayout = findViewById(R.id.swipeRefreshLt)

        recyclerView.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        beersPresenter = BeersPresenter(this)

        srLayout!!.setOnRefreshListener { beersPresenter!!.getBeersData(FIRST_PAGE.toString(), food) }

        etSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                food = getFoodFormatted(etSearch.text.toString())
                if (food!!.isNotEmpty()) {
                    executeSearch()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })

        ivClear.setOnClickListener { resetSearch() }
    }

    /**
     * Execute the search
     */
    private fun executeSearch() {
        showProgressBar()
        beers.clear()
        ivClear.visibility = View.VISIBLE
        food = getFoodFormatted(etSearch.text.toString())
        searchInDBorAPI(food!!)
    }

    override fun onBackPressed() {
        if (food != null) {
            if (food!!.isNotEmpty()) {
                resetSearch()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        showProgressBar()
        beersPresenter!!.getBeersData(FIRST_PAGE.toString(), food)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sort_ascending) {
            sortBeers(ASCENDING)

        } else if (item.itemId == R.id.menu_sort_descending) {
            sortBeers(DESCENDING)
        }
        beersAdapter!!.notifyDataSetChanged()
        layoutManager.scrollToPosition(0)
        return true
    }

    /**
     * Search by key if object was stored in db and get it or make api call if necesary
     *
     * @param food the string as key to search object
     */
    private fun searchInDBorAPI(food: String) {
        if (isSearchStoredInDB(food)) {
            beers.clear()
            val restoredBeers = restoreSearchFromDB(food)
            if (restoredBeers == null || restoredBeers.isEmpty()) {
                showEmptyView()
            } else {
                showBeers(restoredBeers, ASCENDING)
            }

        } else {
            beersPresenter!!.getBeersData(FIRST_PAGE.toString(), food)
        }

    }

    /**
     * Format the search string to use it correctly
     *
     * @param food the string to format
     * @return the formatted string
     */
    private fun getFoodFormatted(food: String): String {
        val formattedFood: String = food.trim { it <= ' ' }.replace(SPACE, UNDERSCORE)
        return formattedFood.toLowerCase()
    }

    /**
     * Hide the soft keyboard
     */
    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Sort and show the data
     *
     * @param beersResult the data received
     */
    override fun sortAndShowBeersData(beersResult: ArrayList<BeerDTO>) {
        recyclerView.visibility = View.VISIBLE
        hideKeyboard()
        showBeers(beersResult, ASCENDING)
    }

    /**
     * Display beers in recyclerview
     *
     * @param beersResult the data to display
     */
    private fun showBeers(beersResult: ArrayList<BeerDTO>, sort: String?) {
        beers.clear()
        beers.addAll(beersResult)

        if (sort != null && sort.isNotEmpty()) {
            sortBeers(sort)
        }

        if (beersAdapter == null) {
            beersAdapter = BeersAdapter(beers)
            recyclerView.adapter = beersAdapter
        } else {
            beersAdapter!!.notifyDataSetChanged()
        }

        srLayout!!.isRefreshing = false
        hideProgressBar()

        deleteFromRealmWithoutKey()

        if (food != null) {
            storeBeers(beersResult, food)
        }
    }

    override fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
    }

    override fun showErrorView() {
        showErrorEmptyLayout(getString(R.string.error))
    }

    override fun showEmptyView() {
        showErrorEmptyLayout(getString(R.string.empty))
    }

    /**
     * Show error or empty view
     *
     * @param message the message to display
     */
    private fun showErrorEmptyLayout(message: String) {
        recyclerView.visibility = View.GONE
        hideKeyboard()
        if (beersAdapter != null) {
            beersAdapter!!.notifyDataSetChanged()
        }
        srLayout!!.isRefreshing = false
        tvMessage!!.text = message
        llErrorEmptyView!!.visibility = View.VISIBLE
    }

    /**
     * Reset search box and make request to show initial data
     */
    private fun resetSearch() {
        layoutManager.scrollToPosition(0)
        llErrorEmptyView!!.visibility = View.INVISIBLE
        ivClear.visibility = View.INVISIBLE
        food = null
        beers.clear()
        etSearch.clearFocus()
        etSearch.setText(EMPTY)
        hideKeyboard()
        showProgressBar()
        beersPresenter!!.getBeersData(FIRST_PAGE.toString(), food)
    }

    /**
     * Sort initial beers data ascending
     *
     * @param order the order to sort
     */
    private fun sortBeers(order: String) {
        if (ASCENDING.equals(order, ignoreCase = true)) {
            beers.sortWith(Comparator { beer1, beer2 -> java.lang.Float.compare(beer1.abv, beer2.abv) })
        } else if (DESCENDING.equals(order, ignoreCase = true)) {
            beers.sortWith(Comparator { beer1, beer2 -> java.lang.Float.compare(beer2.abv, beer1.abv) })
        }

    }

    companion object {

        private const val FIRST_PAGE = 1
        const val EMPTY = ""
        const val SPACE = " "
        const val UNDERSCORE = "_"
        const val ASCENDING = "ascending"
        const val DESCENDING = "descending"
    }
}
