class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :authenticate_user!

  def has_writeaccess_to?(user, resource)
  return false if user.nil? or resource.nil?
  return true if user.user_type == User::TYPES[:ADMIN]



  acl = {
    "CustomersController" => [User::TYPES[:SECRETARY]],
    "UsersController" => [User::TYPES[:SECRETARY]],
    "AddressesController" => [User::TYPES[:SECRETARY]],
    "TimeEntriesController" => [User::TYPES[:SECRETARY], User::TYPES[:EMPLOYEE]]
  }

  acl.fetch(resource, {}).include? user.user_type
end

def authorize_user!
  render :file => "#{Rails.root}/public/403.html", :status => :not_found unless has_authorization?
end

def has_authorization?
  has_writeaccess_to? current_user, self.class.name
end
end
