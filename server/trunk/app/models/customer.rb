class Customer < ActiveRecord::Base
  scope :updated_after, lambda {|last_update| where("updated_at > :last_update OR created_at > :last_update OR deleted_at > :last_update",
    :last_update => last_update) }
end
