# Author

Thierry Vilmart

August 2022

# Summary

A nice synchronization of 2 different UI updates using coroutines and LiveData.

The location from the GPS is displayed every 1s.
The address is fetched from a Geokeo server by using the location every 10s.
The synchronization is done between the two flows as follows. We observe on the main thread for new positions.
And we measure the time with a time. If the time has passed 10 seconds, we update the address.

# Technologies used

RxJava was not used. It was was used in an example variable to show that I can use it correctly, with a disposable and a clear().

We could not force us to use RxJava. LiveData is bound to the UI using "by ... observeAsState()".
We can observe the LiveData using the activity as lifecycleOwner.

And background tasks are run using simple coroutines. We could use Flow but it was not necessary and it was simpler not to use it.

