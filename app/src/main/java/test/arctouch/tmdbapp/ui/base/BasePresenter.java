package test.arctouch.tmdbapp.ui.base;


import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends IPresenterView> {

    private T view;
    private CompositeSubscription compositeSubscription;

    protected void attachView(T view) {
        this.view = view;
        this.compositeSubscription = new CompositeSubscription();
    }

    public void detachView() {
        this.view = null;
        if (!this.compositeSubscription.isUnsubscribed()) {
            this.compositeSubscription.unsubscribe();
        }
        this.compositeSubscription.clear();
        this.compositeSubscription = null;
    }

    protected T getView() {
        return this.view;
    }

}
