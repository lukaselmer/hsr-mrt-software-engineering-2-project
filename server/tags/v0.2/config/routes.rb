Mrt::Application.routes.draw do
  
  resources :time_entry_type_materials
  resources :applied_materials
  resources :materials
  
  resources :addresses

  post 'customers/synchronize(.:format)' => 'customers#synchronize'
  get 'customers/synchronize(.:format)' => 'customers#synchronize'
  resources :customers

  devise_for :users, :controllers => { :sessions => "sessions" }
  devise_scope :user do
    resources :sessions, :only => [:create]
  end

  resources :users

  get 'time_entries/unassigned(.:format)' => 'time_entries#unassigned', :as => :time_entries_unassigned
  get 'time_entries/destroy_all(.:format)' => 'time_entries#destroy_all', :as => :destroy_all_time_entries
  resources :time_entries do
    member do
      post 'remove_hashcode'
    end
  end
  
  resources :orders do
    member do
      get 'add_material'
    end
  end

  post 'time_entry_types/synchronize(.:format)' => 'time_entry_types#synchronize'
  get 'time_entry_types/synchronize(.:format)' => 'time_entry_types#synchronize'
  resources :time_entry_types do
    member do
      get 'add_material'
    end
  end

  # The priority is based upon order of creation:
  # first created -> highest priority.

  # Sample of regular route:
  #   match 'products/:id' => 'catalog#view'
  # Keep in mind you can assign values other than :controller and :action

  # Sample of named route:
  #   match 'products/:id/purchase' => 'catalog#purchase', :as => :purchase
  # This route can be invoked with purchase_url(:id => product.id)

  # Sample resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Sample resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Sample resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Sample resource route with more complex sub-resources
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', :on => :collection
  #     end
  #   end

  # Sample resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end

  # You can have the root of your site routed with "root"
  # just remember to delete public/index.html.
  root :to => "time_entries#index"

  # See how all your routes lay out with "rake routes"

  # This is a legacy wild controller route that's not recommended for RESTful applications.
  # Note: This route will make all actions in every controller accessible via GET requests.
  # match ':controller(/:action(/:id(.:format)))'
end
