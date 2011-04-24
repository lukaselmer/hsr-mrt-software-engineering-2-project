class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :authenticate_user!
  helper_method :write_access?

  def write_access?(object = nil)
    return false if current_user.nil?
    return true if current_user.secretary?
    return false if object.nil?
    return true if object.is_a?(TimeEntry) && (object.new_record? || object.user == current_user)
    return false
  end

  def deny_access!
    render :file => "/public/403.html", :status => 403, :layout => false
  end

  def authorize_secretary!
    deny_access! if current_user.nil? || !current_user.secretary?
  end
end
