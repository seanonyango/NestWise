import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nestwise.data.repository.GoalRepository
import com.example.nestwise.viewmodel.GoalViewModel

class GoalViewModelFactory(
    private val repo: GoalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
