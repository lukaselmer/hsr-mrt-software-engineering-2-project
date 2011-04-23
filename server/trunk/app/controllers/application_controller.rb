class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :authenticate_user!

  def has_writeaccess_to?(user, class_name)
    return false if user.nil? or class_name.nil?
    return true if user.admin?

    # TODO: Funktioniert so nicht, weil ein Mitarbeiter z.B. auf die Kunden Leseberechtigung besitzt, diese aber nicht editieren kann
    acl = {
      "CustomersController" => [User::TYPES[:SECRETARY]],
      "UsersController" => [User::TYPES[:SECRETARY]],
      "AddressesController" => [User::TYPES[:SECRETARY]],
      "TimeEntriesController" => [User::TYPES[:SECRETARY], User::TYPES[:EMPLOYEE]]
    }

    acl.fetch(class_name, {}).include? user.user_type
  end

  def authorize_user!
    # TODO: Maybe 401 would be better?
    render :file => "#{Rails.root}/public/403.html", :status => 403 unless has_authorization?
  end

  def has_authorization?
    has_writeaccess_to? current_user, self.class.name
  end
end
