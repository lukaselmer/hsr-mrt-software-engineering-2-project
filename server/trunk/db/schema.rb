# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20110503185724) do

  create_table "addresses", :force => true do |t|
    t.integer  "gps_position_id"
    t.string   "line1"
    t.string   "line2"
    t.string   "line3"
    t.string   "place"
    t.integer  "zip"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "applied_materials", :force => true do |t|
    t.integer  "material_id"
    t.integer  "order_id"
    t.integer  "amount"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "customers", :force => true do |t|
    t.integer  "address_id"
    t.string   "first_name"
    t.string   "last_name"
    t.string   "phone"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.datetime "deleted_at"
  end

  create_table "gps_positions", :force => true do |t|
    t.decimal  "latitude",   :precision => 15, :scale => 10
    t.decimal  "longitude",  :precision => 15, :scale => 10
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "materials", :force => true do |t|
    t.integer  "material_id"
    t.string   "catalog_id"
    t.text     "description"
    t.string   "dimensions"
    t.decimal  "price",       :precision => 14, :scale => 2
    t.datetime "valid_until"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "orders", :force => true do |t|
    t.integer  "customer_id"
    t.integer  "address_id"
    t.text     "description"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "time_entries", :force => true do |t|
    t.integer  "customer_id"
    t.integer  "time_entry_type_id"
    t.integer  "user_id"
    t.integer  "gps_position_id"
    t.integer  "order_id"
    t.string   "hashcode"
    t.text     "description"
    t.datetime "time_start"
    t.datetime "time_stop"
    t.string   "audio_record_name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "time_entry_type_materials", :force => true do |t|
    t.integer  "time_entry_type_id"
    t.integer  "material_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "time_entry_types", :force => true do |t|
    t.integer  "time_entry_type_id"
    t.text     "description"
    t.datetime "valid_until"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "users", :force => true do |t|
    t.string   "email",                              :default => "", :null => false
    t.string   "encrypted_password",  :limit => 128, :default => "", :null => false
    t.datetime "remember_created_at"
    t.string   "password_salt"
    t.string   "first_name"
    t.string   "last_name"
    t.string   "type"
    t.string   "phone"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "users", ["email"], :name => "index_users_on_email", :unique => true

end
