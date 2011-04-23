class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :authenticate_user!

  def has_writeaccess_to?(user, class_name)
    return false if user.nil? || class_name.nil?
    return true if user.admin?

    # TODO: The permission should be handeled by each controller, and not in the application controller
    # Reason: if a new controller is added, the application controller would have to be changed
    acl = {
      "CustomersController" => [User::TYPES[:SECRETARY]],
      "UsersController" => [User::TYPES[:SECRETARY]],
      "AddressesController" => [User::TYPES[:SECRETARY]],
      "TimeEntriesController" => [User::TYPES[:SECRETARY], User::TYPES[:FIELD_WORKER]]
    }

    acl.fetch(class_name, {}).include? user.user_type
  end

  def authorize_user!
    # TODO: Maybe 401 would be better?
    render :file => "/public/403.html", :status => 403, :layout => false unless has_authorization?
  end

  def has_authorization?
    has_writeaccess_to? current_user, self.class.name
  end
end
