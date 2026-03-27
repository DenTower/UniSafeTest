Реализация приложения:
-

- Данные хранятся в состоянии viewModel, локальная база данных не упоминалась в ТЗ. Параллельно
  обычным спискам используются Map позволяющие по id получать индекс элемента за O(1) вместо O(n)
  при поиске в списке, для того чтобы можно было в первую очередь оперировать через id списков и их
  элементов, во избежание последствий изменений индексов путем обновления данных после чужого
  запроса к серверу.

Основные операции:
- UpdateShoppingList: за O(1) берется элемент который пользователь выбрал для редактирования и
  делается запрос на изменение с новыми значениями для полей элемента
- RemoveShoppingList: за O(1) берется по индексу id списка и отправляется запрос на
  удаление
- RemoveFromList: аналогично Update за O(1) берется по индексу id элемента и также отправляется
  запрос на удаление
- Registration: запрос ключа, сохранение в состоянии, переход к главному экрану O(1)
- MoveItem: не типичная реализация функционала перемещения элемента, а также параметр "listId"
  относится совершенно не к спискам, а к их элементам и должен называться "itemId" или на подобии.
  Реализация следующая, пользователь перетаскивает элемент на новую позицию до или после какого-либо
  другого элемента и, т.к. этот handler лишь присваивает элементу новый порядковый "id"(номер), а
  handler'а для получения этих порядковых номеров не представлено, приходится делать запрос
  изменения порядкового номера для каждого элемента списка O(N), чтобы переставить их в порядок
  соответствующий намериниям пользователя
- CrossItOff: за O(1) берется id элемента по индексу и отправляется запрос
- CreateShoppingList: O(1) запрос на создание списка ключ передан из состояния, название от
  пользователя
- AddToShoppingList: O(1) берется id списка, в который пользователь добавляет элемент и отправляется
  запрос на добавление
- GetShoppingList: получение элементов по id списка, вызывается при каком либо взаимодействии
  пользователя со списком (раскрытие, изменение элементов и т.д.), также происходит за O(N)
  переприсвоение в мапе id to index
- GetAllMyShopLists: получение данных о списках по ключу, вызывается каждые 30 сек и при
  отрисовке главного экрана, также происходит за O(N) переприсвоение в мапе id to index
- Authentication: O(1) вызывается при аунтефикации по ключу, при успехе ключ сохраняется и
  открывается главный экран

This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform
  applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part,
      the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose
  Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run
widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run
widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…